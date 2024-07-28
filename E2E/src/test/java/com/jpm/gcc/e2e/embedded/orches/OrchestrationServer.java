package com.jpm.gcc.e2e.embedded.orches;

import com.jpm.gcc.e2e.embedded.EmbeddedServer;
import com.jpm.gcc.e2e.embedded.ServerException;
import com.jpm.gcc.e2e.embedded.spring.SpringServiceLauncher;
import com.jpm.gcc.e2e.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.test.util.TestSocketUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class OrchestrationServer implements EmbeddedServer {


    private SpringServiceLauncher serviceLauncher;

    private List<String> testFolders = new ArrayList<>();

    private String serviceName = "Orchestration Service";

    @Override
    public String getServerName() {
        return "OrchestrationServer";
    }


    public void initailParameters() throws IOException {
        System.setProperty("embedded.orchestration.service.port.http", String.valueOf(TestSocketUtils.findAvailableTcpPort()));
        ResourceUtil.loadPropertiesTo("src/test/resources/initialization-orchestration.properties", System.getProperties());

        try {
            Map<String, String> map = new HashMap<String, String>();
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            map.put("ABCG_EFG_KEY", "gccd");
            setEnv(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startup() throws ServerException, IOException {
        log.info("Starting {}..", getServerName());
        this.initailParameters();
        this.serviceLauncher = new SpringServiceLauncher(OrchestrationRunner.class.getCanonicalName()).launch();

    }

    @Override
    public void shutdown() {
        log.info("Stopping... {}", getServerName());
        try {
            this.cleanup();
            this.serviceLauncher.stopService();
        } catch (Exception e) {
            log.warn("Exception during stop {} : {}", getServerName(), e.getMessage());
        }
        log.info("Stopped.. {}", getServerName());
    }

    private void cleanup() {
        for (String dir : testFolders) {
            log.info("clean test data folder {}", dir);
            try {
                if (!dir.isEmpty()) FileUtils.deleteDirectory(new File(dir));
            } catch (IOException e) {
                log.warn("File to remove the test folder {} : {}", dir, e);
            }
        }
    }

    protected static void setEnv(Map<String, String> newEnv) throws NoSuchFieldException, IllegalAccessException {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newEnv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newEnv);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newEnv);
                }
            }
        }
    }
}
