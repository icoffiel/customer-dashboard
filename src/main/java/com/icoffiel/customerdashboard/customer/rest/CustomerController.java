package com.icoffiel.customerdashboard.customer.rest;

import com.icoffiel.customerdashboard.customer.business.CustomerService;
import com.icoffiel.customerdashboard.customer.model.Customer;
import com.icoffiel.customerdashboard.customer.rest.model.AddCustomerRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public List<Customer> allCustomers() {
        return customerService.allCustomers();
    }

    @PostMapping("/")
    public CompletableFuture<UUID> createCustomer(@RequestBody AddCustomerRequest request) {
        return customerService.addCustomer(request);
    }
}
