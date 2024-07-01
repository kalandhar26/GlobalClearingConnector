package com.hdfc.imps.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class KafkaTopicConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic createTopic() {
        return new NewTopic("hdfc-topic", 1, (short) 1);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers));
    }
}
