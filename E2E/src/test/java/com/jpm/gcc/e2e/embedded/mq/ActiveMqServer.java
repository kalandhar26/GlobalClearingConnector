package com.jpm.gcc.e2e.embedded.mq;

import com.jpm.gcc.e2e.embedded.EmbeddedServer;
import com.jpm.gcc.e2e.embedded.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;

import java.io.IOException;

@Slf4j
public class ActiveMqServer implements EmbeddedServer {

    private BrokerService server;

    @Override
    public String getServerName() {
        return "Embedded Active MQ";
    }

    @Override
    public void startup() throws ServerException, IOException {

        try {
            int port = 61616;

            System.setProperty("local.mq.broker.url", String.format("tcp://localhost:%d", port));
            System.setProperty("local.mq.broker.host","localhost");
            System.setProperty("local.mq.broker.port", String.valueOf(port));
            System.setProperty("local.mq.broker.type","amq");

            String activeMqBrokerUrl = System.getProperty("local.mq.broker.url");
            server = new BrokerService();
            server.addConnector(activeMqBrokerUrl);
            server.setPersistent(false);
            server.getSystemUsage().getMemoryUsage().setLimit(100 * 1024 * 1024);
            server.setUseJmx(false);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));

        } catch (Exception e) {
            throw new ServerException("Initial Exception", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            log.info("Failed to stop embedded activeMQ {}", e.getMessage());
        }
    }
}
