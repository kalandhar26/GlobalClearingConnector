package com.jpm.gcc;

import com.jpm.gcc.e2e.SystemProperties;
import com.jpm.gcc.e2e.embedded.EmbeddedServers;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Cucumber.class)
@CucumberOptions(features = "",
stepNotifications = true,
glue={""},
plugin={"pretty","json:target/surefire-reports/cucumber.json"})
public class ITRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITRunner.class);

    public static void steup() throws Exception{
        SystemProperties.init();
        EmbeddedServers.startAll();
        LOGGER.info("Starting E2ERunner, BeforeClass...");
    }

    @AfterClass
    public static void teardown() throws Exception{
        LOGGER.info("Stopping E2ERunner, AfterClass...");
        EmbeddedServers.stopAll();
        LOGGER.info("Stopping E2ERunner, Done");
    }
}
