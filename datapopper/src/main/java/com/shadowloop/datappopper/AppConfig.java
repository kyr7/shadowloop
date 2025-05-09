package com.shadowloop.datappopper;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TestBean beanOne() {
        return new TestBean();
    }

}