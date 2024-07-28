package com.jpm.gcc.e2e.embedded.spring;

import com.jpm.gcc.e2e.embedded.ServerException;
import org.springframework.cloud.client.ServiceInstance;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class SpringServiceLauncher {

    private final String[] launcherClasses;

    private final List<ServiceInstance> activeServiceInstances = new ArrayList<>();

    public SpringServiceLauncher(String... launcherClasses) {
        this.launcherClasses = launcherClasses;
    }

    public SpringServiceLauncher launch() throws ServerException {
        try {
            for (String serviceClassName : launcherClasses) {
                launchApp(serviceClassName);
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopService()));
        } catch (Exception e) {
            throw new ServerException("Cannot start service", e);
        }
        return this;
    }


    public void stopService() {
        for (ServiceInstance serviceInstance : activeServiceInstances) {
            ((ServiceRunner) serviceInstance.runnerInstance).stop();
        }
    }

    private void launchApp(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        URL[] urls = new URL[0];
        if (contextLoader instanceof URLClassLoader) {
            urls = ((URLClassLoader) contextLoader).getURLs();
        }

        ClassLoader classLoader = new URLClassLoader(urls, contextLoader);
        Class<?> runnerClass = classLoader.loadClass(className);
        Object runnerInstance = runnerClass.newInstance();

        final ServiceInstance serviceInstance = new ServiceInstance(runnerInstance);
        activeServiceInstances.add(serviceInstance);
        runnerClass.getMethod("run").invoke(runnerInstance);
    }

    private class ServiceInstance {
        private Object runnerInstance;

        public ServiceInstance(final Object runnerInstance) {
            this.runnerInstance = runnerInstance;
        }
    }
}
