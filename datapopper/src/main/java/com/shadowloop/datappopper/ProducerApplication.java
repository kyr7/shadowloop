package com.shadowloop.datappopper;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableAutoConfiguration
@Import({ AppConfigProducer.class, AppConfigConsumer.class })
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Autowired
    KafkaTopicCreator kafkaTopicCreator;

    @Autowired(required = false)
    KafkaListenerBean kafkaListenerBean;

    @PostConstruct
    public void init() {
        kafkaTopicCreator.createTopic("topic1", 10, 1);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        return args -> {
            template.send("topic1", "Hello Kafka");
        };
    }

}
