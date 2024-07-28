package com.jpm.gcc.e2e.embedded.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ServiceRunner {

    private ConfigurableApplicationContext appContext;

    private final Class<?>[] serviceClasses;

    private Object monitor = new Object();

    private boolean shouldWait;

    protected ServiceRunner(Class<?>... serviceClasses) {
        this.serviceClasses = serviceClasses;
    }

    public void run() {
        if (appContext != null) {
            throw new IllegalStateException("AppContext must be null to run this service");
        }
        runServiceInThread();
        waitUntilServiceIsStarted();
    }

    private void waitUntilServiceIsStarted() {
        try {
            synchronized (monitor) {
                while (shouldWait) {
                    monitor.wait();
                }
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


    private void runServiceInThread() {
        final Thread runnerThread = new ServiceRunnerThread();
        shouldWait = true;
        runnerThread.setContextClassLoader(serviceClasses[0].getClassLoader());
        runnerThread.start();
    }

    public void stop() {
        if (appContext != null) {
            SpringApplication.exit(appContext);
            appContext = null;
        }
    }


    private class ServiceRunnerThread extends Thread {
        @Override
        public void run() {
            appContext = SpringApplication.run(serviceClasses, new String[]{});
            synchronized (monitor) {
                shouldWait = false;
                monitor.notifyAll();
            }
        }
    }


}
