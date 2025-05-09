package com.shadowloop.datappopper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
public class AppConfigConsumer {

    @Bean
    @ConditionalOnProperty(name = "create.kafka.listener", havingValue = "true")
    KafkaListenerBean kafkaListenerBean() {
        return new KafkaListenerBean();
    }

}

class KafkaListenerBean {
    @KafkaListener(id = "myId", topics = "topic1")
    public void listen(String in) {
        System.out.println(in);
    }
}
