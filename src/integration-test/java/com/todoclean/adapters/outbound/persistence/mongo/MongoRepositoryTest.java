package com.todoclean.adapters.outbound.persistence.mongo;

import com.todoclean.TodoSpringApplication;
import org.bson.BsonDocument;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

// no mocking == no spring context refresh == fast test
// slice test == light-weight spring context == fast start
// using real database (via test containers)  == reliance
// container instance shared across all tests

@Testcontainers
@DataMongoTest
@ContextConfiguration(classes = { TodoSpringApplication.class, MongoConfig.class })
public abstract class MongoRepositoryTest {

    @Rule
    protected static final MongoDBContainer MONGO = SharedTestMongoContainer.getInstance();

    private static final BsonDocument ALL_DOCUMENTS = new BsonDocument();

    @Autowired
    protected MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanup() {
        mongoTemplate.getCollectionNames()
                .forEach(this::truncateCollection);
    }

    private void truncateCollection(String collectionName) {
        mongoTemplate.getCollection(collectionName)
                .deleteMany(ALL_DOCUMENTS);
    }
}
