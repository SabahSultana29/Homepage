package com.bank.creditcard.service;

import com.bank.creditcard.model.Customer;
import com.bank.creditcard.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
//CustomerService.java file

2️⃣ OfferService.java (updated to match DTOs)

package com.bank.creditcard.service;

import com.bank.creditcard.dto.AcceptOfferRequest;
import com.bank.creditcard.dto.AcceptOfferResponse;
import com.bank.creditcard.model.CreditCardAccount;
import com.bank.creditcard.model.CreditCardOffer;
import com.bank.creditcard.repository.CreditCardAccountRepository;
import com.bank.creditcard.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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

    public AcceptOfferResponse acceptOffer(AcceptOfferRequest request) {
        Optional<CreditCardOffer> offerOpt = offerRepository.findById(request.getOfferId());
        if (offerOpt.isEmpty()) {
            return new AcceptOfferResponse(false, "Offer not found", null);
        }

        CreditCardOffer offer = offerOpt.get();

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

        return new AcceptOfferResponse(true, "Card created successfully!", account.getCardNumber());
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}


---

3️⃣ EmailService.java

package com.bank.creditcard.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendConfirmation(Long customerId, String cardNumber) {
        // In real life, send email via SMTP/SES/etc.
        System.out.println("Email sent to Customer " + customerId +
                " with card number " + cardNumber);
    }
}


---

4️⃣ PrintShopService.java

package com.bank.creditcard.service;

import org.springframework.stereotype.Service;

@Service
public class PrintShopService {

    public void printCard(Long accountId) {
        // Simulate printing process
        System.out.println("Card printing request raised for account ID: " + accountId);
    }
}


---

5️⃣ TransactionService.java

package com.bank.creditcard.service;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public void processTransaction(Long accountId, double amount) {
        // Dummy logic for transaction simulation
        System.out.println("Processing transaction of amount ₹" + amount + " for account ID: " + accountId);
    }
}




✅ Now you have all 5 service classes ready:

CustomerService

OfferService

EmailService

PrintShopService

TransactionService