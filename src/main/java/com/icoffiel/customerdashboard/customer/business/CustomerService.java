package com.icoffiel.customerdashboard.customer.business;

import com.icoffiel.customerdashboard.configuration.TopicConfig;
import com.icoffiel.customerdashboard.customer.kafka.CustomerStreams;
import com.icoffiel.customerdashboard.customer.model.Customer;
import com.icoffiel.customerdashboard.customer.model.CustomerEvent;
import com.icoffiel.customerdashboard.customer.model.CustomerEventType;
import com.icoffiel.customerdashboard.customer.rest.model.AddCustomerRequest;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {
    private final KafkaTemplate<UUID, CustomerEvent> kafkaTemplate;
    private final StreamsBuilderFactoryBean factoryBean;

    public CustomerService(KafkaTemplate<UUID, CustomerEvent> kafkaTemplate, StreamsBuilderFactoryBean factoryBean) {
        this.kafkaTemplate = kafkaTemplate;
        this.factoryBean = factoryBean;
    }

    public CompletableFuture<UUID> addCustomer(AddCustomerRequest request) {

        UUID eventId = UUID.randomUUID();
        return kafkaTemplate
                .send(
                        TopicConfig.CUSTOMER_EVENT_TOPIC,
                        eventId,
                        new CustomerEvent(
                                eventId,
                                CustomerEventType.ADD_CUSTOMER,
                                new Customer(
                                        UUID.randomUUID(),
                                        request.firstName(),
                                        request.lastName(),
                                        request.email()
                                )
                        )
                )
                .thenApply(result -> result.getProducerRecord().key());
    }

    public List<Customer> allCustomers() {
        KafkaStreams streams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<UUID, Customer> store = streams.store(
                StoreQueryParameters.fromNameAndType(
                        CustomerStreams.CUSTOMER_TABLE_STORE,
                        QueryableStoreTypes.keyValueStore()
                )
        );

        KeyValueIterator<UUID, Customer> results = store.all();

        List<Customer> customers = new ArrayList<>();
        results.forEachRemaining(keyValue -> customers.add(keyValue.value));

        return customers;
    }
}
