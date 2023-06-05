package com.icoffiel.customerdashboard.customer.model;

import java.util.UUID;

public record Customer(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
