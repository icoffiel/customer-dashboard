package com.icoffiel.customerdashboard.customer.rest.model;

public record AddCustomerRequest(
    String firstName,
    String lastName,
    String email
) {
}
