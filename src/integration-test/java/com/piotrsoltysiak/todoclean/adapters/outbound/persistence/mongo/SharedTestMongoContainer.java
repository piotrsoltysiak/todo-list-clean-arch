package com.piotrsoltysiak.todoclean.adapters.outbound.persistence.mongo;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

class SharedTestMongoContainer extends MongoDBContainer {

    private static final String IMAGE_VERSION = "mongo:3.6.0";

    private static SharedTestMongoContainer mongoContainer;

    private SharedTestMongoContainer() {
        super(DockerImageName.parse(IMAGE_VERSION));
    }

    static SharedTestMongoContainer getInstance() {
        if (mongoContainer == null) {
            mongoContainer = new SharedTestMongoContainer();
            mongoContainer.start();
        }
        return mongoContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.data.mongodb.uri", getReplicaSetUrl());
    }

    @Override
    public void close() {
        //do nothing, JVM handles shut down
    }

}
