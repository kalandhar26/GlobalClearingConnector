package com.jpm.gcc.e2e.embedded.kafka;

import com.jpm.gcc.e2e.embedded.EmbeddedServer;
import com.jpm.gcc.e2e.embedded.ServerException;
import com.jpm.gcc.e2e.util.ResourceUtil;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaServer implements EmbeddedServer {

    public static Map<String, BlockingQueue<ConsumerRecord<String, Object>>> consumerQueues = new HashMap<>();

    private static final AtomicBoolean isCleanupInProgress = new AtomicBoolean(false);

    public static final String TOPIC_SUFFIX = "em1700";

    private static EmbeddedKafkaRule kafka;

    @Override
    public String getServerName() {
        return "Embedded Kafka Service.!";
    }

    @Override
    public void startup() throws ServerException, IOException {
        String[] topics = this.getArrayProperty("kafka.initialization.topics", false);
        log.info("Starting the Embedded Kafka with topics of {}", Strings.join(topics, ","));
        kafka = new EmbeddedKafkaRule(1, true, 1, topics);
        kafka.before();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
        postServiceStart();
        log.info("Starting the Embedded Kafka");
        String[] consumers = this.getArrayProperty("kafka.initialization.consumers", false);
        for (String consumer : consumers) {
            createConsumer(consumer);
        }
    }

    @Override
    public void shutdown() {
        log.info("Stopping embedded Kafka");
        if (kafka != null) {
            kafka.after();
        }
        log.info("Stopped Embedded Kafka");
    }

    public static void clear() {
        isCleanupInProgress.set(true);
        consumerQueues.values().forEach(consumerRecords -> {
            try {
                while (consumerRecords.size() != 0) {
                    consumerRecords.take();
                }
            } catch (InterruptedException e) {
                log.info(e.getMessage(), e);
            } finally {
                consumerRecords.clear();
            }
        });

        isCleanupInProgress.set(false);
    }

    private String[] getArrayProperty(String propertyName, boolean allowEmptyValue) {
        Properties properties = ResourceUtil.loadProperties("initialiation-kafka-properties");
        String splitRegex = allowEmptyValue ? "\\s*,\\s*" : "\\s*,[,\\s]*";
        return properties.getProperty(propertyName).trim().split(splitRegex);
    }

    private void postServiceStart() {
        System.setProperty("kafka.bootstrap-servers", kafka.getEmbeddedKafka().getBrokersAsString());
        System.setProperty("graphite.kafka.metadataBrokersList", kafka.getEmbeddedKafka().getBrokersAsString());
        //System.setProperty("kafka.zookeeper.hosts",kafka.getEmbeddedKafka().getZookeeperConnectionString());
        System.setProperty("kafka.zookeeper.hosts", kafka.getEmbeddedKafka().getBrokersAsString());
        System.setProperty("pes.kafka.metadata.broker.list", kafka.getEmbeddedKafka().getBrokersAsString());
        System.setProperty("graphite.kafka.metadata-broker-list", kafka.getEmbeddedKafka().getBrokersAsString());
        System.setProperty("graphite.kafka.bootstrap-servers", kafka.getEmbeddedKafka().getBrokersAsString());
    }

    private void createConsumer(String topicName) {
        log.info("Adding test consumer for {} topic", topicName);
        KafkaMessageListenerContainer<String, Object> container = createConsumerContainer(topicName);
        final BlockingQueue<ConsumerRecord<String, Object>> recordQ = new LinkedBlockingQueue<>();

        consumerQueues.put(topicName, recordQ);
        container.setupMessageListener((MessageListener<String, Object>) record -> {
            if (!isCleanupInProgress.get()) {
                log.info("Received a message on kafka topic {}", topicName);
                recordQ.add(record);
            } else {
                log.info("Cleanup in progress");
                log.info("Message received {}", record);
            }
        });

        container.setBeanName(topicName + "e2e-test-assert-bean");
        container.start();
    }

    private KafkaMessageListenerContainer<String, Object> createConsumerContainer(String topicName) {
        // create a new Consumer Container for the topic

        String consumerGroupId = topicName + "-gc-02061997";
        Map<String, Object> kafkaConsumerProperties = KafkaTestUtils.consumerProps(kafka.getEmbeddedKafka().getBrokersAsString(), consumerGroupId, "false");
        kafkaConsumerProperties.put("key.deserializer", StringDeserializer.class);
        kafkaConsumerProperties.put("auto.offset.reset", "latest");
        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaConsumerProperties);
        ContainerProperties consumerProperties = new ContainerProperties(topicName);
        consumerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);
        return new KafkaMessageListenerContainer<>(consumerFactory, consumerProperties);
    }

    public static KafkaTemplate<String, String> createProducer() {
        Map<String, Object> kafkaProducerProperties = KafkaTestUtils.producerProps(kafka.getEmbeddedKafka());
        kafkaProducerProperties.replace("key.Serializer", StringSerializer.class.getCanonicalName());
        log.info("embedded broker name is :" + kafkaProducerProperties.get("bootstrap.servers"));
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProducerProperties);
        return new KafkaTemplate<>(producerFactory);
    }

    private KafkaTemplate<String, String> createSCProducer(String topicName) {
        Map<String, Object> kafkaProducerProperties = KafkaTestUtils.producerProps(kafka.getEmbeddedKafka());
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProducerProperties);
        return new KafkaTemplate<>(producerFactory);
    }

    public void publish(String message, String topicName) {
        KafkaTemplate<String, String> kafkaTemplate = createProducer();
        topicName = topicName + "-em1800";
        kafkaTemplate.setDefaultTopic(topicName);
        Map<String, String> headers = Collections.singletonMap("encrypted", "true");

        try {
            MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message);
            headers.forEach((k, v) -> {
                messageBuilder.setHeader(k, String.valueOf(v).getBytes());
            });
        } catch (Exception e) {
            log.error("{} Exception occurred while publishing via the Embedded Kafka to topic {}", e, topicName);
        }

        kafkaTemplate.flush();
    }

}

