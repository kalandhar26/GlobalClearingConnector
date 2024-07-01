package com.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Object> jsonKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Object> customKafkaTemplate;

    public void sendStringMessage(String topic, String message) {
        stringKafkaTemplate.send(topic, message);
    }

    public void sendJsonMessage(String topic, Object message) {
        jsonKafkaTemplate.send(topic, message);
    }

    public void sendCustomMessage(String topic, Object message) {
        customKafkaTemplate.send(topic, message);
    }

}

