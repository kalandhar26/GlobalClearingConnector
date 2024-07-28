package com.jpm.gcc.e2e.embedded.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class MainApplication {

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getContext() {
        return applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
