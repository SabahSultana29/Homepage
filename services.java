
### ✅ `CustomerService.java`
package com.bank.creditcard.service;

import com.bank.creditcard.model.Customer;
import com.bank.creditcard.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get customer by ID
    public ResponseEntity<Customer> getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // Save new customer
    public ResponseEntity<Customer> saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}
```

---

### ✅ `OfferService.java`

```java
package com.bank.creditcard.service;

import com.bank.creditcard.dto.AcceptOfferRequest;
import com.bank.creditcard.dto.AcceptOfferResponse;
import com.bank.creditcard.model.CreditCardAccount;
import com.bank.creditcard.model.CreditCardOffer;
import com.bank.creditcard.repository.CreditCardAccountRepository;
import com.bank.creditcard.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final CreditCardAccountRepository accountRepository;
    private final EmailService emailService;
    private final PrintShopService printShopService;

    public OfferService(OfferRepository offerRepository,
                        CreditCardAccountRepository accountRepository,
                        EmailService emailService,
                        PrintShopService printShopService) {
        this.offerRepository = offerRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.printShopService = printShopService;
    }

    // Accept an offer and create account
    public ResponseEntity<AcceptOfferResponse> acceptOffer(AcceptOfferRequest request) {
        CreditCardOffer offer = offerRepository.findById(request.getOfferId()).orElse(null);

        if (offer == null) {
            return new ResponseEntity<>(
                    new AcceptOfferResponse(false, "Offer not found", null),
                    HttpStatus.NOT_FOUND
            );
        }

        CreditCardAccount account = new CreditCardAccount();
        account.setCustomerId(request.getCustomerId());
        account.setCardNumber(generateCardNumber());
        account.setCardType(offer.getCardType());
        account.setCreditLimit(offer.getCreditLimit());
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        emailService.sendConfirmation(account.getCustomerId(), account.getCardNumber());
        printShopService.printCard(account.getId());

        AcceptOfferResponse response =
                new AcceptOfferResponse(true, "Card created successfully!", account.getCardNumber());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Helper method to generate a random card number
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
```

---

### ✅ `EmailService.java`

```java
package com.bank.creditcard.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Send confirmation email (simulated)
    public ResponseEntity<String> sendConfirmation(Long customerId, String cardNumber) {
        String message = "Email sent to Customer ID " + customerId +
                " with Card Number: " + cardNumber;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
```

---

### ✅ `PrintShopService.java`

```java
package com.bank.creditcard.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrintShopService {

    // Print physical card request (simulated)
    public ResponseEntity<String> printCard(Long accountId) {
        String message = "Print request created for Account ID: " + accountId;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
```

---

### ✅ `TransactionService.java`

```java
package com.bank.creditcard.service;

import com.bank.creditcard.model.Transaction;
import com.bank.creditcard.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get all transactions
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Save new transaction
    public ResponseEntity<Transaction> saveTransaction(Transaction transaction) {
        Transaction saved = transactionRepository.save(transaction);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get transaction by ID
    public ResponseEntity<Transaction> getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}

