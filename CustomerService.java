package com.scb.creditcardorigination.userStory6.service;
import com.scb.creditcardorigination.userStory6.model.Customer;
import com.scb.creditcardorigination.userStory6.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(long id) {
       return
               customerRepository.findById(id).orElse(null);

    }


    public ResponseEntity<Customer> saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}