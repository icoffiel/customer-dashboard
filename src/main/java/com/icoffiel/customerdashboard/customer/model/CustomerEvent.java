package com.icoffiel.customerdashboard.customer.model;

import java.util.UUID;

public record CustomerEvent(
        UUID id,
        CustomerEventType type,
        Customer customer
) {
}
