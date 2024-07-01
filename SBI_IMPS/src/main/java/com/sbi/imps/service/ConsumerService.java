package com.sbi.imps.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import static com.sbi.imps.constants.BasicConstants.QUEUE_NAME;

@Service
public class ConsumerService {
    @JmsListener(destination = QUEUE_NAME)
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
