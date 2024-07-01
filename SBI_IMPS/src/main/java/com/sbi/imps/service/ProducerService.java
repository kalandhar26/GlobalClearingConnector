package com.sbi.imps.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static com.sbi.imps.constants.BasicConstants.QUEUE_NAME;

@Service
public class ProducerService {
    private final JmsTemplate jmsTemplate;

    public ProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String message){
        jmsTemplate.convertAndSend(QUEUE_NAME,message);
    }
}
