package com.jivesoftware.datagenerator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import com.jivesoftware.datagenerator.model.List;
import com.jivesoftware.datagenerator.model.Result;
import io.nodus.Nodus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by erwolff on 4/1/15.
 */

@Singleton
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DataGeneratorResource {

    private static final Logger log = LoggerFactory.getLogger(DataGeneratorResource.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @GET
    @Path("/api/core/v3/contents")
    public String getTestData(@QueryParam("count") Integer count) {
        Result r = new Result();
        r.setItemsPerPage(count);
        ObjectNode result = Nodus.builder().instance(r).build().getJson();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < count; i++) {
            arrayNode.add(Nodus.builder(List.class).build().getJson());
        }
        result.put("list", arrayNode);
        return result.toString();
    }
}
