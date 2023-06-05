package com.icoffiel.customerdashboard.customer.kafka;

import com.icoffiel.customerdashboard.configuration.TopicConfig;
import com.icoffiel.customerdashboard.customer.model.Customer;
import com.icoffiel.customerdashboard.customer.model.CustomerEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerStreams {

    public static final String CUSTOMER_TABLE_STORE = "customer_table_store";

    @Bean
    public KStream<UUID, Customer> customerEventToCustomer(StreamsBuilder builder) {
        KStream<UUID, Customer> customerStream = builder
                .stream(TopicConfig.CUSTOMER_EVENT_TOPIC, Consumed.with(Serdes.UUID(), new JsonSerde<>(CustomerEvent.class)))
                .map((key, value) -> new KeyValue<>(value.customer().id(), value.customer()));

        customerStream.to(TopicConfig.CUSTOMER_TOPIC, Produced.with(Serdes.UUID(), new JsonSerde<>(Customer.class)));

        return customerStream;
    }

    @Bean
    public GlobalKTable<UUID, Customer> customerTable(StreamsBuilder builder) {
        return builder.globalTable(
                TopicConfig.CUSTOMER_TOPIC,
                Materialized
                        .<UUID, Customer>as(Stores.persistentKeyValueStore(CUSTOMER_TABLE_STORE))
                        .withKeySerde(Serdes.UUID())
                        .withValueSerde(new JsonSerde<>(Customer.class))
        );
    }
}
