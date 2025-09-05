//package com.scb.creditcardorigination.userStory6.service;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferRequest;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
//import com.scb.creditcardorigination.userStory6.dto.EmailRequest;
//import com.scb.creditcardorigination.userStory6.dto.OfferAcceptanceRequest;
//import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
//import com.scb.creditcardorigination.userStory6.model.Customer;
//import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class OfferOrchestrationService {
//
//    private final OfferService offerService;
//    private final CustomerService customerService;
//    private final EmailService emailService;
//    private final PrintShopService printShopService;
//
//    public OfferOrchestrationService(
//            OfferService offerService,
//            CustomerService customerService,
//            EmailService emailService,
//            PrintShopService printShopService
//    ) {
//        this.offerService = offerService;
//        this.customerService = customerService;
//        this.emailService = emailService;
//        this.printShopService = printShopService;
//    }
//
//    public ResponseEntity<AcceptOfferResponse> processOfferAcceptance(OfferAcceptanceRequest request) {
//        // Step 1: Save Customer
//        Customer customer = new Customer();
//        customer.setName(request.getCustomerName());
//        customer.setEmail(request.getEmail());
//        customer.setPhone(request.getPhone());
//        ResponseEntity<Customer> savedCustomer = customerService.saveCustomer(customer);
//
//        // Step 2: Call OfferService
//        AcceptOfferRequest offerRequest = new AcceptOfferRequest(
//                request.getOfferId(),
//                request.getOfferName(),
//                request.getDescription(),
//                request.getAnnualFee()
//        );
//        ResponseEntity<AcceptOfferResponse> offerResponse = offerService.acceptOffer(offerRequest);
//
//        // Step 3: Create CreditCardAccount
//        CreditCardAccount account = new CreditCardAccount();
//        account.setCardNumber(request.getCardNumber());
//        account.setEmailId(request.getEmail());
//        account.setCustomer(savedCustomer.getBody());
//        account.setCreatedAt(LocalDateTime.now());
//        // 👉 Save account if you have a CreditCardAccountRepository
//
//        // Step 4: Send Email
//        EmailRequest emailRequest = new EmailRequest();
//        emailRequest.setEmailId(request.getEmail());
//        emailRequest.setCardNumber(request.getCardNumber());
//        emailService.sendConfirmation(emailRequest.getEmailId(), emailRequest.getCardNumber());
//
//        // Step 5: PrintShop Request
//        PrintShopRequest psr = new PrintShopRequest();
//        psr.setAccountId(account.getId());
//        psr.setStatus("PENDING");
//        psr.setDetails("Print card for customer: " + request.getCustomerName());
//        psr.setRequestTime(LocalDateTime.now());
//        printShopService.saveRequest(psr);
//
//        return offerResponse;
//    }
//
//    public ResponseEntity<String> processOfferDecline(Long offerId) {
//        return offerService.declineOffer(offerId);
//    }
//}


package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
import com.scb.creditcardorigination.userStory6.dto.OfferAcceptanceRequest;
import com.scb.creditcardorigination.userStory6.dto.EmailRequest;
import com.scb.creditcardorigination.userStory6.model.Customer;
import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OfferOrchestrationService {

    private final OfferService offerService;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final PrintShopService printShopService;
    private final CreditCardAccountService creditCardAccountService;

    public OfferOrchestrationService(
            OfferService offerService,
            CustomerService customerService,
            EmailService emailService,
            PrintShopService printShopService,
            CreditCardAccountService creditCardAccountService
    ) {
        this.offerService = offerService;
        this.customerService = customerService;
        this.emailService = emailService;
        this.printShopService = printShopService;
        this.creditCardAccountService = creditCardAccountService;
    }

    public ResponseEntity<AcceptOfferResponse> processOfferAcceptance(OfferAcceptanceRequest request) {
        // Step 1: Save Customer
        Customer customer = new Customer();
        customer.setName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer savedCustomer = customerService.saveCustomer(customer).getBody();

        // Step 2: Save Credit Card Account
        CreditCardAccount account = new CreditCardAccount();
        account.setCustomer(savedCustomer);
        account.setCardNumber(request.getCardNumber());
        account.setOffer(null); // if you don’t map full object, can set offer later
        account.setCreatedAt(LocalDateTime.now());
        account.setEmailId(request.getEmail());

        CreditCardAccount savedAccount = creditCardAccountService.saveAccount(account);

        // Step 3: Accept the offer
        ResponseEntity<AcceptOfferResponse> offerResponse = offerService.acceptOffer(request);

        // Step 4: Send confirmation email
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmailId(request.getEmail());
        emailRequest.setCardNumber(request.getCardNumber());
        emailService.sendConfirmation(emailRequest.getEmailId(), emailRequest.getCardNumber());

        // Step 5: Create PrintShopRequest
        PrintShopRequest psr = new PrintShopRequest();
        psr.setAccountId(savedAccount.getId());
        psr.setStatus("PENDING");
        psr.setDetails("Printing request for card " + request.getCardNumber());
        psr.setRequestTime(LocalDateTime.now());
        printShopService.saveRequest(psr);

        return offerResponse;
    }

    public ResponseEntity<String> processOfferDecline(Long offerId) {
        return offerService.declineOffer(offerId);
    }
}

