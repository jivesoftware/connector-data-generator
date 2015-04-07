package com.jivesoftware.datagenerator;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Module;
import com.jivesoftware.service.base.ServiceHandle;
import com.jivesoftware.service.restful.server.RestfulServerFactory;
import com.jivesoftware.service.restful.server.RestfulServerInitializer;
import com.jivesoftware.service.restful.server.RestfulServerUI;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by erwolff on 4/1/15.
 */
public class DataGeneratorMainInitializer extends RestfulServerInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataGeneratorMainInitializer.class);

    public static final Map<String, String> DEFAULT_CONFIG = ImmutableMap.<String, String>builder()
            .put(IS_INITIALLY_ELIGIBLE_FOR_LOAD_BALANCING, "true")
            .put(MAX_QUEUED_REQUESTS, "10000")
            .put(MAX_NUMBER_OF_THREADS, "64")
            .put(PORT, "19400")
            .build();

    public static ServiceHandle initialize(Map<String, String> configuration, List<String> endpointPackages, List<Module> modules, ThreadPool threadPool) {
        int port = Integer.parseInt(configuration.get(PORT));

        return RestfulServerFactory.createServer(port, endpointPackages, modules, new RestfulServerUI(), threadPool);
    }
}
