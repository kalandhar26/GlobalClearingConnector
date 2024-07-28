package com.jpm.gcc.e2e.embedded;

import com.jpm.gcc.e2e.embedded.hsqldb.HsqlDbServer;
import com.jpm.gcc.e2e.embedded.kafka.KafkaServer;
import com.jpm.gcc.e2e.embedded.mq.ActiveMqServer;
import com.jpm.gcc.e2e.embedded.orches.OrchestrationServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.with;

public class EmbeddedServers {
    private static Logger LOGGER = LoggerFactory.getLogger(EmbeddedServers.class);

    private static List<EmbeddedServer> serverList;

    private static Path pidFile;

    static {
        serverList = new ArrayList<>();
        serverList.add(new KafkaServer());
        serverList.add(new ActiveMqServer());
        serverList.add(new HsqlDbServer());
        serverList.add(new OrchestrationServer());
    }

    public static void startAll() throws ServerException, IOException {
        LOGGER.warn("Starting all Services");

        for (EmbeddedServer server : serverList) {
            server.startup();
        }

        postServicesStart();
    }

    public static void stopAll() throws ServerException, IOException {
        String stopEmbeddedServicesManually = System.getProperty("e2e.stopEmbeddedSrv.manually", "false");
        if (Boolean.valueOf(stopEmbeddedServicesManually)) {
            stopServicesLater();
        } else {
            stopServicesNow();
        }
    }

    public static void stopServicesLater() {
        LOGGER.info("Please stop embedded servers by removing {} after investigate test data.", pidFile);
        with().await().forever().until(
                () -> {
                    boolean stopService = Files.notExists(pidFile);
                    if (stopService) {
                        stopServicesNow();
                    }
                    return stopService;
                }
        );
    }

    public static void stopServicesNow() {
        LOGGER.info("Stopping All Services..");
        for(EmbeddedServer embeddedServer: serverList){
            try{
                embeddedServer.shutdown();
            }catch (Exception e){
                LOGGER.warn("Some exception during shutdown the service {} : {}",embeddedServer.getServerName(), e.getMessage());
            }
        }
    }

    private static void postServicesStart(){
        pidFile = Paths.get(System.getProperty("project.pid.file"));
        try{
            Files.deleteIfExists(pidFile);
            Files.createFile(pidFile);
        }catch(Exception e){
            LOGGER.warn("PID file {} has not been created", pidFile);
        }
    }

    public static List<EmbeddedServer> getServerList(){
        return serverList;
    }

    public static void setServerList(List<EmbeddedServer> serverList){
        EmbeddedServers.serverList = serverList;
    }



}
