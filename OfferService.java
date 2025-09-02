//Updated OfferService.java
package com.bank.creditcard.service;

import com.bank.creditcard.model.CreditCardOffer;
import com.bank.creditcard.model.CreditCardAccount;
import com.bank.creditcard.repository.CreditCardOfferRepository;
import com.bank.creditcard.repository.CreditCardAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService {

    private final CreditCardOfferRepository offerRepo;
    private final CreditCardAccountRepository accountRepo;
    private final EmailService emailService;
    private final PrintShopService printShopService;

    public OfferService(CreditCardOfferRepository offerRepo,
                        CreditCardAccountRepository accountRepo,
                        EmailService emailService,
                        PrintShopService printShopService) {
        this.offerRepo = offerRepo;
        this.accountRepo = accountRepo;
        this.emailService = emailService;
        this.printShopService = printShopService;
    }

    @Transactional
    public String acceptOffer(Long offerId) {
        Optional<CreditCardOffer> optionalOffer = offerRepo.findById(offerId);
        if (optionalOffer.isPresent()) {
            CreditCardOffer offer = optionalOffer.get();

            if (!"pending".equalsIgnoreCase(offer.getStatus())) {
                return "Offer already handled";
            }

            // Update offer status to accepted
            offer.setStatus("accepted");
            offerRepo.save(offer);

            // Create credit card account
            CreditCardAccount account = new CreditCardAccount();
            account.setCustomerId(offer.getCustomerId());
            account.setCreditLimit(offer.getCreditLimit());
            account.setStatus("active");

            // Generate dummy 16-digit card number
            String generatedCardNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            account.setCardNumber(generatedCardNumber);
            accountRepo.save(account);

            // Send confirmation email and print to console
            emailService.sendConfirmation(offer.getCustomerId(), generatedCardNumber);

            // Create printshop request and print to console
            printShopService.createPrintRequest(account.getId(), "Print and ship physical credit card");

            return "Offer accepted, account created, email sent, and printshop request initiated.";
        } else {
            return "Offer not found.";
        }
    }
}
//Email log repository
package com.bank.creditcard.repository;

import com.bank.creditcard.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}

//Email service
package com.bank.creditcard.service;

import com.bank.creditcard.model.EmailLog;
import com.bank.creditcard.repository.EmailLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final EmailLogRepository emailLogRepository;

    public EmailService(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }

    public void sendConfirmation(Long customerId, String cardNumber) {
        // Save email log entry
        EmailLog emailLog = new EmailLog();
        emailLog.setCustomerId(customerId);
        emailLog.setCardNumber(cardNumber);
        emailLog.setMessage("Your credit card has been created successfully!");
        emailLog.setSentAt(LocalDateTime.now());

        emailLogRepository.save(emailLog);

        // Print to console
        System.out.println("📧 Email Sent Log: " + emailLog);
    }
}

//PrintShopRequestRepository
package com.bank.creditcard.repository;

import com.bank.creditcard.model.PrintShopRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintShopRequestRepository extends JpaRepository<PrintShopRequest, Long> {
}

//PrintShopService
package com.bank.creditcard.service;

import com.bank.creditcard.model.PrintShopRequest;
import com.bank.creditcard.repository.PrintShopRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PrintShopService {

    private final PrintShopRequestRepository printShopRepository;

    public PrintShopService(PrintShopRequestRepository printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    public void createPrintRequest(Long accountId, String details) {
        PrintShopRequest request = new PrintShopRequest();
        request.setAccountId(accountId);
        request.setDetails(details);
        request.setRequestTime(LocalDateTime.now());
        request.setStatus("queued");

        printShopRepository.save(request);

        // Print to console
        System.out.println("🖨️ PrintShop Request Created: " + request);
    }
}

//Print shop controller
package com.bank.creditcard.controller;

import com.bank.creditcard.model.PrintShopRequest;
import com.bank.creditcard.repository.PrintShopRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/print-requests")
public class PrintShopController {

    private final PrintShopRepository printShopRepository;

    public PrintShopController(PrintShopRepository printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    @GetMapping
    public List<PrintShopRequest> getAllPrintRequests() {
        return printShopRepository.findAll();
    }
}


//Email log controller
package com.bank.creditcard.controller;

import com.bank.creditcard.model.EmailLog;
import com.bank.creditcard.repository.EmailLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email-logs")
public class EmailLogController {

    private final EmailLogRepository emailLogRepository;

    public EmailLogController(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }

    @GetMapping
    public List<EmailLog> getAllLogs() {
        return emailLogRepository.findAll();
    }
}

//Modal for Email_log
package com.bank.creditcard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_log")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String cardNumber;

    @Column(length = 1000)
    private String message;

    private LocalDateTime sentAt;

    // Constructors
    public EmailLog() {}

    public EmailLog(Long customerId, String cardNumber, String message, LocalDateTime sentAt) {
        this.customerId = customerId;
        this.cardNumber = cardNumber;
        this.message = message;
        this.sentAt = sentAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "EmailLog{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", cardNumber='" + cardNumber + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

