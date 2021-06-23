package com.todoclean.adapters.outbound.persistence.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.todoclean.adapters.outbound.persistence.mongo")
class MongoConfig {

}
