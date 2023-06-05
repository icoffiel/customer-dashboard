package com.icoffiel.customerdashboard.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {
    public static final String CUSTOMER_EVENT_TOPIC = "customer_events";
    public static final String CUSTOMER_TOPIC = "customer";

    @Bean
    public NewTopic customerEventTopic() {
        return TopicBuilder
                .name(CUSTOMER_EVENT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic customerTopic() {
        return TopicBuilder
                .name(CUSTOMER_TOPIC)
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }
}
