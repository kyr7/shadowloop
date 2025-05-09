package com.shadowloop.datappopper;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Configuration
public class AppConfig {

    // Configure KafkaAdmin to manage Kafka topics
    @Bean
    public KafkaAdmin kafkaAdmin() {
        KafkaAdmin admin = new KafkaAdmin(Collections.singletonMap("bootstrap.servers", "localhost:9092"));
        admin.setFatalIfBrokerNotAvailable(true); // Fail fast if Kafka broker is unavailable
        return admin;
    }

    // Provide AdminClient for dynamic topic creation
    @Bean
    public AdminClient adminClient(KafkaAdmin kafkaAdmin) {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    // Service for creating topics dynamically at runtime
    @Bean
    public KafkaTopicCreator kafkaTopicCreator(AdminClient adminClient) {
        return new KafkaTopicCreator(adminClient);
    }
}

// Utility class for dynamic topic creation
class KafkaTopicCreator {

    private final AdminClient adminClient;

    public KafkaTopicCreator(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void createTopic(String topicName, int partitions, int replicas) {
        try {
            NewTopic newTopic = TopicBuilder.name(topicName)
                    .partitions(partitions)
                    .replicas(replicas)
                    .build();
            adminClient.createTopics(Collections.singleton(newTopic)).all().get(); // Synchronous call
            System.out.println("Topic created: " + topicName);
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException) {
                System.out.println("Topic already exists: " + topicName);
            } else {
                throw new RuntimeException("Failed to create topic: " + topicName, e);
            }
        }
    }
}