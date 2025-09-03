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

//updated printShopService

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

//updated offerService.java
package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.dto.AcceptOfferRequest;
import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
import com.scb.creditcardorigination.userStory6.model.CreditCardOffer;
import com.scb.creditcardorigination.userStory6.repository.CreditCardAccountRepository;
import com.scb.creditcardorigination.userStory6.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    @Transactional
    public ResponseEntity<AcceptOfferResponse> acceptOffer(AcceptOfferRequest request) {
        CreditCardOffer offer = offerRepository.findById(request.getOfferId()).orElse(null);
        
        if (offer == null) {
            return new ResponseEntity<>(
                new AcceptOfferResponse(false, "Offer not found"), 
                HttpStatus.NOT_FOUND
            );
        }

        CreditCardAccount account = new CreditCardAccount();
        account.setId(request.getOfferId());
        account.setCardNumber(generateCardNumber());
        account.setCardNumber(offer.getCardNumber());
        account.setCreatedAt(offer.getCreditLimit());
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        accountRepository.save(account);

        emailService.sendConfirmation(account.getId(), account.getCardNumber());
        printShopService.printCard(account.getId());

        AcceptOfferResponse response = 
            new AcceptOfferResponse(true, "Card created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public List<CreditCardOffer> getAllOffers() {
        return offerRepository.findAll();
    }
}

updated printshopservice
package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import com.scb.creditcardorigination.userStory6.repository.PrintShopRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PrintShopService {

    private final PrintShopRequestRepository printShopRepository;

    public PrintShopService(PrintShopRequestRepository printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    public void printCard(Long accountId) {
        PrintShopRequest request = new PrintShopRequest();
        request.setAccountId(accountId);
        request.setDetails("Print and ship physical credit card");
        request.setRequestTime(LocalDateTime.now());
        request.setStatus("queued");
        
        printShopRepository.save(request);

        // Print to console
        System.out.println("🖨️ PrintShop Request Created: " + request);
    }
}

//updated emailService.java
public void sendConfirmation(Long customerId, String cardNumber) {
    // Save email log entry
    EmailLog emailLog = new EmailLog();
    emailLog.setCustomerId(customerId);
    emailLog.setCardNumber(cardNumber);

    // Add subject + body
    String subject = "Credit Card Creation Confirmation";
    String body = "Dear Customer,\n\nYour credit card with number " + cardNumber +
                  " has been created successfully.\n\nThank you for choosing our services.\n\nRegards,\nBank Team";

    emailLog.setMessage("Subject: " + subject + "\n\nBody:\n" + body);
    emailLog.setSentAt(LocalDateTime.now());

    emailLogRepository.save(emailLog);

    // Print formatted email in console
    System.out.println("===== EMAIL SENT =====");
    System.out.println("To Customer ID: " + customerId);
    System.out.println("Card Number: " + cardNumber);
    System.out.println("Subject: " + subject);
    System.out.println("Body:\n" + body);
    System.out.println("======================");
}

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailLogRepository emailLogRepository;

    public EmailService(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }

    public void sendConfirmation(Long customerId, String cardNumber) {
        // Save email log entry
        EmailLog emailLog = new EmailLog();
        emailLog.setCustomerId(customerId);
        emailLog.setCardNumber(cardNumber);

        String subject = "Credit Card Creation Confirmation";
        String body = "Dear Customer,\n\nYour credit card with number " + cardNumber +
                      " has been created successfully.\n\nThank you for choosing our services.\n\nRegards,\nBank Team";

        emailLog.setMessage("Subject: " + subject + "\n\nBody:\n" + body);
        emailLog.setSentAt(LocalDateTime.now());

        emailLogRepository.save(emailLog);

        // ✅ Use logger instead of System.out
        log.info("\n===== EMAIL SENT =====\n" +
                 "To Customer ID: {}\n" +
                 "Card Number: {}\n" +
                 "Subject: {}\n" +
                 "Body:\n{}\n" +
                 "======================",
                 customerId, cardNumber, subject, body);
    }
}

@RestController
@RequestMapping("/api/email_logs")
public class EmailLogController {

    private final EmailLogRepository emailLogRepository;
    private final EmailService emailService;  // ✅ inject service (not static)

    // ✅ Constructor injection
    public EmailLogController(EmailLogRepository emailLogRepository, EmailService emailService) {
        this.emailLogRepository = emailLogRepository;
        this.emailService = emailService;
    }

    // ✅ GET all logs
    @GetMapping
    public List<EmailLog> getAllLogs() {
        return emailLogRepository.findAll();
    }

    // ✅ POST: send email with JSON body
    @PostMapping("/send")
    public Map<String, String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendConfirmation(request.getCustomerId(), request.getCardNumber());  // ✅ use injected service

        Map<String, String> response = new HashMap<>();
        response.put("status", "Email Sent Successfully");
        response.put("customerId", String.valueOf(request.getCustomerId()));
        response.put("cardNumber", request.getCardNumber());
        return response;
    }
}

//Updated Prtintshop controller 
@RestController
@RequestMapping("/api/printshop")
public class PrintshopController {

    private final PrintshopService printshopService;

    public PrintshopController(PrintshopService printshopService) {
        this.printshopService = printshopService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendToPrintshop(@RequestBody PrintshopRequest request) {
        printshopService.saveRequest(request);
        return ResponseEntity.ok("Print request sent to PrintShop successfully!");
    }

    @GetMapping("/requests")
    public List<PrintshopRequest> getAllRequests() {
        return printshopService.getAllRequests();
    }
}


//Updated offerservice
@Service
public class OfferService {

    private final PrintshopService printshopService;

    public OfferService(PrintshopService printshopService) {
        this.printshopService = printshopService;
    }

    public void acceptOffer(String customerName, String cardNumber, String address) {
        // business logic for accepting offer

        // create a PrintshopRequest object
        PrintshopRequest request = new PrintshopRequest();
        request.setCustomerName(customerName);
        request.setCardNumber(cardNumber);
        request.setAddress(address);

        // save to printshop
        printshopService.saveRequest(request);

        // later this can be extended to actually notify external print system via API
    }
}

//updated printshop controller code
@RestController
@RequestMapping("/api/printShop")
public class PrintShopController {

    private final PrintShopService printshopService;

    public PrintShopController(PrintShopService printshopService) {
        this.printshopService = printshopService;
    }

    @PostMapping("/request")
    public ResponseEntity<PrintShopRequest> sendToPrintShop(@RequestBody PrintShopRequest request) {
        PrintShopRequest savedRequest = printshopService.saveRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/requests")
    public List<PrintShopRequest> getAllRequests() {
        return printshopService.getAllRequests();
    }
}

//updated printing service 
@Service
public class PrintShopService {

    @Autowired
    private final PrintShop printShopRepository;

    public PrintShopService(PrintShop printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {
        PrintShopRequest saved = printShopRepository.save(request);
        System.out.println("✅ PrintShop Request Created: " +
                "ID=" + saved.getId() +
                ", AccountID=" + saved.getAccountId() +
                ", Status=" + saved.getStatus() +
                ", Details=" + saved.getDetails() +
                ", RequestTime=" + saved.getRequestTime());
        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();
        System.out.println("📋 All PrintShop Requests:");
        for (PrintShopRequest req : requests) {
            System.out.println("   -> ID=" + req.getId() +
                    ", AccountID=" + req.getAccountId() +
                    ", Status=" + req.getStatus() +
                    ", Details=" + req.getDetails() +
                    ", RequestTime=" + req.getRequestTime());
        }
        return requests;
    }

    public void printCard(long id) {
        System.out.println("🖨️ Printing card for PrintShop Request ID: " + id);
    }
}

//updated print shop service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrintShopService {

    private final PrintShopRepository printShopRepository;

    @Autowired
    public PrintShopService(PrintShopRepository printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {
        PrintShopRequest saved = printShopRepository.save(request);
        System.out.println("✅ PrintShop Request Created:");
        System.out.println("ID=" + saved.getId() +
                ", AccountID=" + saved.getAccountId() +
                ", Status=" + saved.getStatus() +
                ", Details=" + saved.getDetails() +
                ", RequestTime=" + saved.getRequestTime());
        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();
        System.out.println("📌 All PrintShop Requests:");

        // ✅ FIXED for-loop
        for (PrintShopRequest req : requests) {
            System.out.println("ID=" + req.getId() +
                    ", AccountID=" + req.getAccountId() +
                    ", Status=" + req.getStatus() +
                    ", Details=" + req.getDetails() +
                    ", RequestTime=" + req.getRequestTime());
        }

        return requests;
    }

    // print a single card (simulate printing)
    public void printCard(long id) {
        PrintShopRequest req = printShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));

        System.out.println("🖨️ Printing card for PrintShop Request:");
        System.out.println("ID=" + req.getId() +
                ", AccountID=" + req.getAccountId() +
                ", Status=" + req.getStatus() +
                ", Details=" + req.getDetails() +
                ", RequestTime=" + req.getRequestTime());
    }
}

//to be corrected code
package com.scb.creditcardorigination.userStory6.service;
import com.scb.creditcardorigination.userStory6.repository.PrintShop;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrintShopService {

    private final PrintShop printShopRepository;

    @Autowired
    public PrintShopService(PrintShop printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {
        PrintShopRequest saved = printShopRepository.save(request);
        System.out.println("PrintShop Request Created:");
        System.out.println("ID=" + saved.getId() +
                ", AccountID=" + saved.getAccountId() +
                ", Status=" + saved.getStatus() +
                ", Details=" + saved.getDetails() +
                ", RequestTime=" + saved.getRequestTime());
        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();
        System.out.println("All PrintShop Requests:");
        for (PrintShopRequest req : requests) {
            System.out.println("ID=" + req.getId() +
                    ", AccountID=" + req.getAccountId() +
                    ", Status=" + req.getStatus() +
                    ", Details=" + req.getDetails() +
                    ", RequestTime=" + req.getRequestTime());
        }

        return requests;
    }
    public void printCard(long id) {
        PrintShopRequest req = printShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));
        System.out.println("Printing card for PrintShop Request :");
        System.out.println("ID=" + req.getId() +
                ", AccountID=" + req.getAccountId() +
                ", Status=" + req.getStatus() +
                ", Details=" + req.getDetails() +
                ", RequestTime=" + req.getRequestTime());
    }
}

//Updated printshopservice code 

package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.repository.PrintShop;
import com.scb.creditcardorigination.userStory6.repository.CustomerRepository;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import com.scb.creditcardorigination.userStory6.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrintShopService {

    private final PrintShop printShopRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public PrintShopService(PrintShop printShopRepository, CustomerRepository customerRepository) {
        this.printShopRepository = printShopRepository;
        this.customerRepository = customerRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {
        // ✅ fetch customer from DB
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.getCustomerId()));

        // ✅ set accountId automatically from customer
        request.setAccountId(customer.getAccountId());

        // ✅ set request time if not provided
        if (request.getRequestTime() == null) {
            request.setRequestTime(LocalDateTime.now());
        }

        PrintShopRequest saved = printShopRepository.save(request);

        System.out.println("✅ PrintShop Request Created:");
        System.out.println("ID=" + saved.getId() +
                ", CustomerID=" + saved.getCustomerId() +
                ", AccountID=" + saved.getAccountId() +
                ", Status=" + saved.getStatus() +
                ", Details=" + saved.getDetails() +
                ", RequestTime=" + saved.getRequestTime());

        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();

        System.out.println("📋 All PrintShop Requests:");
        for (PrintShopRequest req : requests) {
            System.out.println("ID=" + req.getId() +
                    ", CustomerID=" + req.getCustomerId() +
                    ", AccountID=" + req.getAccountId() +
                    ", Status=" + req.getStatus() +
                    ", Details=" + req.getDetails() +
                    ", RequestTime=" + req.getRequestTime());
        }

        return requests;
    }

    // print card
    public void printCard(long id) {
        PrintShopRequest req = printShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));

        System.out.println("🖨️ Printing card for PrintShop Request:");
        System.out.println("ID=" + req.getId() +
                ", CustomerID=" + req.getCustomerId() +
                ", AccountID=" + req.getAccountId() +
                ", Status=" + req.getStatus() +
                ", Details=" + req.getDetails() +
                ", RequestTime=" + req.getRequestTime());
    }
}


//updated printshop service.java
package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.repository.PrintShop;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PrintShopService {

    private final PrintShop printShopRepository;

    // 🔹 Define a formatter for requestTime
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PrintShopService(PrintShop printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {

        // 🔹 Auto-set requestTime if not provided
        if (request.getRequestTime() == null) {
            request.setRequestTime(LocalDateTime.now());
        }

        // 🔹 Auto-update accountId if not provided (increment latest one)
        if (request.getAccountId() == null) {
            Long maxId = printShopRepository.findAll()
                    .stream()
                    .mapToLong(PrintShopRequest::getAccountId)
                    .max()
                    .orElse(1000L); // default start
            request.setAccountId(maxId + 1);
        }

        PrintShopRequest saved = printShopRepository.save(request);

        System.out.println("✅ PrintShop Request Created:");
        System.out.println("➡️ Updated AccountID: " + saved.getAccountId());
        System.out.println("➡️ Updated RequestTime: " + saved.getRequestTime().format(FORMATTER));
        System.out.println("Status=" + saved.getStatus());
        System.out.println("Details=" + saved.getDetails());

        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();
        System.out.println("📌 All PrintShop Requests:");
        for (PrintShopRequest req : requests) {
            System.out.println("----------------------------");
            System.out.println("ID=" + req.getId());
            System.out.println("➡️ Updated AccountID: " + req.getAccountId());
            System.out.println("➡️ Updated RequestTime: " + 
                    (req.getRequestTime() != null ? req.getRequestTime().format(FORMATTER) : "N/A"));
            System.out.println("Status=" + req.getStatus());
            System.out.println("Details=" + req.getDetails());
        }
        return requests;
    }

    // print card
    public void printCard(long id) {
        PrintShopRequest req = printShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));

        System.out.println("🖨️ Printing card for PrintShop Request:");
        System.out.println("ID=" + req.getId());
        System.out.println("➡️ Updated AccountID: " + req.getAccountId());
        System.out.println("➡️ Updated RequestTime: " + 
                (req.getRequestTime() != null ? req.getRequestTime().format(FORMATTER) : "N/A"));
        System.out.println("Status=" + req.getStatus());
        System.out.println("Details=" + req.getDetails());
    }
}




