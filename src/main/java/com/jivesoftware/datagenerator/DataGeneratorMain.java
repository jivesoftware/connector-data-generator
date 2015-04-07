package com.jivesoftware.datagenerator;

import com.google.inject.Module;
import com.jivesoftware.service.base.ServiceHandle;
import com.jivesoftware.service.configuration.ConfigurationPropertiesProvider;
import com.jivesoftware.service.configuration.UncaughtExceptionsInitializer;
import com.jivesoftware.service.restful.endpoints.loadbalancer.LoadBalanceEligiblityGuiceModule;
import com.jivesoftware.service.restful.server.RestfulServerFactory;
import com.jivesoftware.service.restful.server.util.MonitorableThreadPool;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.jivesoftware.service.restful.server.RestfulServerInitializer.IS_INITIALLY_ELIGIBLE_FOR_LOAD_BALANCING;
import static com.jivesoftware.service.restful.server.RestfulServerInitializer.MAX_NUMBER_OF_THREADS;
import static com.jivesoftware.service.restful.server.RestfulServerInitializer.MAX_QUEUED_REQUESTS;
import static com.jivesoftware.datagenerator.DataGeneratorMainInitializer.DEFAULT_CONFIG;

/**
 * Created by erwolff on 4/1/15.
 */
public class DataGeneratorMain implements ServiceHandle {

    private static final Logger log = LoggerFactory.getLogger(DataGeneratorMain.class);

    private final ServiceHandle serviceHandle;

    public DataGeneratorMain(Map<String, String> configuration, Module... modules) {
        UncaughtExceptionsInitializer.initializeMemoryExceptionsHandler();

        MonitorableThreadPool threadPool = RestfulServerFactory.createThreadPool(
                Integer.parseInt(configuration.get(MAX_NUMBER_OF_THREADS)),
                Integer.parseInt(configuration.get(MAX_QUEUED_REQUESTS))
        );

        boolean loadBalancerEligible = Boolean.parseBoolean(configuration.get(IS_INITIALLY_ELIGIBLE_FOR_LOAD_BALANCING));

        List<Module> runModules = Arrays.asList(ArrayUtils.addAll(modules,
                        new LoadBalanceEligiblityGuiceModule(loadBalancerEligible))
        );

        List<String> endpointPackages = Arrays.asList(
                "com.jivesoftware.datagenerator.api"
        );

        serviceHandle = DataGeneratorMainInitializer.initialize(configuration, endpointPackages, runModules, threadPool);
    }

    public void start() throws Exception {
        serviceHandle.start();
    }

    public void stop() throws Exception {
        serviceHandle.stop();
    }

    public static void main(String[] args) throws Exception {
        try {

            ConfigurationPropertiesProvider cpp = new ConfigurationPropertiesProvider(Arrays.asList(
                    DEFAULT_CONFIG
            ));
            Map<String, String> configuration = cpp.getConfiguration(args);
            DataGeneratorMain server = new DataGeneratorMain(configuration);

            server.start();
            System.out.println("Server is Running");
        } catch (Throwable t) {
            log.error("Error running server", t);
        }
    }
}
