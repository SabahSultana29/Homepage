package com.scb.creditcardorigination.userStory6.controller;
import com.scb.creditcardorigination.userStory6.model.Customer;
import com.scb.creditcardorigination.userStory6.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    //api : get customer by id
@GetMapping("/{id}")
    public ResponseEntity<Customer>getCustomerById(@PathVariable long id){
       Customer customer = customerService.getCustomerById(id);
       if(customer == null){
           return
                   ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(customer);
}

//api : create new customer
   @PostMapping
   public ResponseEntity<Customer>
   createCustomer(@RequestBody Customer customer){
        return
           customerService.saveCustomer(customer);
   }

}
