package com.jpm.gcc.e2e;

import com.jpm.gcc.e2e.util.ResourceUtil;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SystemProperties {

    public static void init() throws Exception {
        Path testClassPath = Paths.get(new ClassPathResource("initialization.properties/../").getFile().getAbsolutePath());
        System.setProperty("project.dir.test", formatPath(testClassPath));
        System.setProperty("project.dir.target", formatPath(testClassPath.getParent()));
        System.setProperty("project.dir.root", formatPath(testClassPath.getParent().getParent().getParent()));

        System.setProperty("project.pid.file", String.format("%s/ gcc-starter-e2e.lck", formatPath(testClassPath.getParent())));

        Properties systemProperties = ResourceUtil.loadProperties("src/test/resources/initialization.properties");

        systemProperties.forEach((key, value) -> {
            String val = ResourceUtil.replacePlaceHolders(String.valueOf(value), System.getProperties());
            System.setProperty(String.valueOf(key), val);
        });
    }

    private static String formatPath(Path path) {
        return path.toAbsolutePath().toString().replaceAll("\\\\", "/");
    }
}

