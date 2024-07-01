package com.hdfc.imps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    @KafkaListener(topics = "hdfc-topic", groupId = "hdfc-consumergroup")
    public void consume(String xmlContent) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String message = xmlMapper.readValue(xmlContent, String.class);
            System.out.println("message " + message);
            log.info("received the data : {} ", message);

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
