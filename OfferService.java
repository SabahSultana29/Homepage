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

//New feature TP
//transaction.java modal class
package com.scb.tps.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String name;
    private String dob;
    private String email;
    private String phone;
    private int creditScore;
    private String product;          // Card type / product
    private String validityPeriod;   // e.g., 2025-2030
    private String creditLimit;      // e.g., ₹2,00,000
    private String status;           // Approved
    private LocalDate approvalDate;  // Auto-filled

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(String validityPeriod) { this.validityPeriod = validityPeriod; }

    public String getCreditLimit() { return creditLimit; }
    public void setCreditLimit(String creditLimit) { this.creditLimit = creditLimit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
}

//transaction controller.java
package com.scb.tps.controller;

import com.scb.tps.model.Transaction;
import com.scb.tps.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
//tp repo 
package com.scb.tps.repository;

import com.scb.tps.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

//tp service.java
package com.scb.tps.service;

import com.scb.tps.model.Transaction;
import com.scb.tps.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        // Auto-fill approval date if not provided
        if (transaction.getApprovalDate() == null) {
            transaction.setApprovalDate(LocalDate.now());
        }

        Transaction saved = transactionRepository.save(transaction);

        // ✅ Print transaction details in console
        System.out.println("✅ New Transaction Created:");
        System.out.println("ID = " + saved.getId()
                + ", CustomerID = " + saved.getCustomerId()
                + ", Name = " + saved.getName()
                + ", DOB = " + saved.getDob()
                + ", Email = " + saved.getEmail()
                + ", Phone = " + saved.getPhone()
                + ", Credit Score = " + saved.getCreditScore()
                + ", Product = " + saved.getProduct()
                + ", Validity = " + saved.getValidityPeriod()
                + ", Credit Limit = " + saved.getCreditLimit()
                + ", Status = " + saved.getStatus()
                + ", Approval Date = " + saved.getApprovalDate());

        return saved;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        // ✅ Print all transactions when fetching
        System.out.println("📋 Fetching All Transactions:");
        for (Transaction tx : transactions) {
            System.out.println("ID = " + tx.getId()
                    + ", CustomerID = " + tx.getCustomerId()
                    + ", Name = " + tx.getName()
                    + ", Product = " + tx.getProduct()
                    + ", Status = " + tx.getStatus()
                    + ", Approval Date = " + tx.getApprovalDate());
        }

        return transactions;
    }
}

//schema.sql 
DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    credit_score INT NOT NULL,
    product VARCHAR(50) NOT NULL,
    validity_period VARCHAR(50),
    credit_limit VARCHAR(50),
    status VARCHAR(20),
    approval_date DATE
);
//data.sql
INSERT INTO transactions (customer_id, name, dob, email, phone, credit_score, product, validity_period, credit_limit, status, approval_date)
VALUES 
(1001, 'Abhishek Kale', '2001-08-12', 'abhishek@example.com', '+91-9876543210', 782, 'Platinum Rewards Card', '2025-2030', '₹2,00,000', 'Approved', '2025-09-02'),

(1002, 'Riya Sharma', '1998-05-21', 'riya@example.com', '+91-9988776655', 810, 'Gold Travel Card', '2025-2030', '₹1,50,000', 'Approved', '2025-09-03'),

(1003, 'Arjun Mehta', '1995-02-15', 'arjun@example.com', '+91-9123456789', 650, 'Basic Cashback Card', '2025-2030', '₹75,000', 'Approved', '2025-09-01');

//json body
{
  "customerId": 1005,
  "name": "Karan Singh",
  "dob": "1996-04-20",
  "email": "karan@example.com",
  "phone": "+91-9876112233",
  "creditScore": 720,
  "product": "Platinum Rewards Card",
  "validityPeriod": "2025-2030",
  "creditLimit": "₹1,80,000",
  "status": "Approved"
}

//schema.sql
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    credit_score INT NOT NULL,
    product VARCHAR(100) NOT NULL,
    validity_period VARCHAR(20) NOT NULL,
    credit_limit VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    approval_date DATE NOT NULL
);

//data.sql
INSERT INTO transactions 
(customer_id, name, dob, email, phone, credit_score, product, validity_period, credit_limit, status, approval_date)
VALUES
(1001, 'Sabah Sultana', '2001-08-12', 'sabah@example.com', '+91-9876543210', 782, 'Platinum Credit Card', '2025-2030', '₹2,00,000', 'Approved', '2025-09-02'),
(1002, 'Aryan Sharma', '1998-05-21', 'aryan@example.com', '+91-9988776655', 810, 'Gold Credit Card', '2025-2030', '₹1,50,000', 'Approved', '2025-09-02'),
(1003, 'Abhishek Kale', '1995-02-15', 'abhi@example.com', '+91-9123456789', 650, 'Silver Credit Card', '2025-2030', '₹1,00,000', 'Approved', '2025-09-01');

//Transaction controller code
package com.scb.creditcardorigination.TransactionProcessingSystem.controller;

import com.scb.creditcardorigination.TransactionProcessingSystem.model.Transaction;
import com.scb.creditcardorigination.TransactionProcessingSystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
    @RestController
    @RequestMapping("/api/transactions")
    public class TransactionController {
        private final TransactionService transactionService;
        public TransactionController(TransactionService transactionService) {
            this.transactionService = transactionService;
        }
        @PostMapping("/create")
        public Transaction createTransaction(@RequestBody Transaction transaction) {
            return transactionService.saveTransaction(transaction);
        }
        @GetMapping("/all")
        public List<Transaction> getAllTransactions() {
            return transactionService.getAllTransactions();
        }
    }

//sales application table code 
import React, { useState } from "react";
import "./SalesApplicationTable.css";
 
function SalesApplicationsTable() {
  // 🔹 Fixed timeline options
  const timelineStages = [
    "Application Submitted",
    "Application Processing",
    "Credit Card Offered",
    "Application Accepted",
    "Printed",
    "Shipped",
  ];
 
  // 🔹 Demo Data
  const [applications, setApplications] = useState([
    {
      applicationId: "APP1001",
      name: "Abhishek Kale",
      dob: "2001-08-12",
      email: "abhishek@example.com",
      contactNumber: "+91-9876543210",
      employmentType: "Salaried",
      monthlyIncome: "₹75,000",
      creditScore: 782,
      productType: "Platinum Rewards Card",
      status: "Approved",
      assignedLimit: "₹2,00,000",
      cardLast4: "5678",
      approvalDate: "2025-09-02",
      processedBy: "System Auto-Decision",
      applicationTimeline: "Application Submitted",
    },
    {
      applicationId: "APP1002",
      name: "Riya Sharma",
      dob: "1998-05-21",
      panNumber: "RIYSH1234K",
      email: "riya@example.com",
      contactNumber: "+91-9988776655",
      employmentType: "Self-Employed",
      monthlyIncome: "₹1,20,000",
      creditScore: 810,
      productType: "Gold Travel Card",
      status: "Pending",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "-",
      processedBy: "Underwriter01",
      applicationTimeline: "Application Processing",
    },
    {
      applicationId: "APP1003",
      name: "Arjun Mehta",
      dob: "1995-02-15",
      panNumber: "ARJMH9876L",
      email: "arjun@example.com",
      contactNumber: "+91-9123456789",
      employmentType: "Salaried",
      monthlyIncome: "₹60,000",
      creditScore: 650,
      productType: "Basic Cashback Card",
      status: "Rejected",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "2025-09-01",
      processedBy: "Underwriter02",
      applicationTimeline: "Credit Card Offered",
    },
  ]);
 
  // 🔹 Handle timeline change
  const handleTimelineChange = (id, newTimeline) => {
    setApplications((prevApps) =>
      prevApps.map((app) =>
        app.applicationId === id ? { ...app, applicationTimeline: newTimeline } : app
      )
    );
  };
 
  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>
 
      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>Application ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Assigned Limit</th>
                <th>Approval Date</th>
                <th>Processed By</th>
                <th>Application Timeline</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.applicationId}>
                  <td>{app.applicationId}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.contactNumber}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.productType}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.assignedLimit}</td>
                  <td>{app.approvalDate}</td>
                  <td>{app.processedBy}</td>
                  <td>
                    <select
                      value={app.applicationTimeline}
                      onChange={(e) =>
                        handleTimelineChange(app.applicationId, e.target.value)
                      }
                    >
                      {timelineStages.map((stage, index) => (
                        <option key={index} value={stage}>
                          {stage}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}
 
export default SalesApplicationsTable;

//updated react code
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./SalesApplicationTable.css";

function SalesApplicationsTable() {
  const [applications, setApplications] = useState([]);

  // fetch data from backend
  useEffect(() => {
    axios.get("http://localhost:8080/api/transactions/all")
      .then(response => {
        setApplications(response.data);
      })
      .catch(error => {
        console.error("Error fetching transactions:", error);
      });
  }, []);

  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>

      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Credit Limit</th>
                <th>Approval Date</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.id}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.phone}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.product}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.creditLimit}</td>
                  <td>{app.approvalDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}

export default SalesApplicationsTable;
//updated controller 
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    ...
}

//controller code 



package com.scb.creditcardorigination.TransactionProcessingSystem.controller;

import com.scb.creditcardorigination.TransactionProcessingSystem.model.Transaction;
import com.scb.creditcardorigination.TransactionProcessingSystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
    @CrossOrigin(origins = "http://localhost:3000")
    @RestController
    @RequestMapping("/api/transactions")
    public class TransactionController {
        private final TransactionService transactionService;
        public TransactionController(TransactionService transactionService) {
            this.transactionService = transactionService;
        }
        @PostMapping("/create")
        public Transaction createTransaction(@RequestBody Transaction transaction) {
            return transactionService.saveTransaction(transaction);
        }
        @GetMapping("/all")
        public List<Transaction> getAllTransactions() {
            return transactionService.getAllTransactions();
        }
    }

//tservice code
package com.scb.creditcardorigination.TransactionProcessingSystem.service;
import com.scb.creditcardorigination.TransactionProcessingSystem.model.Transaction;
import org.springframework.stereotype.Service;
import com.scb.creditcardorigination.TransactionProcessingSystem.repository.TransactionRepository;
import java.time.LocalDate;
import java.util.List;
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        // Auto-fill approval date if not provided
        if (transaction.getApprovalDate() == null) {
            transaction.setApprovalDate(LocalDate.now());
        }
        Transaction saved = transactionRepository.save(transaction);

        System.out.println("New Transaction Created:");
        System.out.println("\n ID = " + saved.getId()
                + "\n CustomerID = " + saved.getCustomerId()
                + "\n Name = " + saved.getName()
                + "\n DOB = " + saved.getDob()
                + "\n email = " + saved.getEmail()
                + "\n  Phone = " + saved.getPhone()
                + "\n  Credit Score = " + saved.getCreditScore()
                + "\n  Product = " + saved.getProduct()
                + "\n  Validity = " + saved.getValidityPeriod()
                + "\n  Credit Limit = " + saved.getCreditLimit()
                + "\n  Status = " + saved.getStatus()
                + "\n Approval Date = " + saved.getApprovalDate());

        return saved;
    }
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("Fetching All Transactions:");
        for (Transaction tx : transactions) {
            System.out.println("ID = " + tx.getId()
                    + ", CustomerID = " + tx.getCustomerId()
                    + ", Name = " + tx.getName()
                    + ", Product = " + tx.getProduct()
                    + ", Status = " + tx.getStatus()
                    + ", Approval Date = " + tx.getApprovalDate());
        }

        return transactions;
    }
}

//tmodal code
package com.scb.creditcardorigination.TransactionProcessingSystem.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String name;
    private String dob;
    private String email;
    private String phone;
    private int creditScore;
    private String product;
    private String validityPeriod;
    private String creditLimit;
    private String status;
    private LocalDate approvalDate;  // Auto-filled
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(String validityPeriod) { this.validityPeriod = validityPeriod; }

    public String getCreditLimit() { return creditLimit; }
    public void setCreditLimit(String creditLimit) { this.creditLimit = creditLimit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
}

//trepo
package com.scb.creditcardorigination.TransactionProcessingSystem.repository;

import com.scb.creditcardorigination.TransactionProcessingSystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

//react code 
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./SalesApplicationTable.css";

function SalesApplicationsTable() {
  const [applications, setApplications] = useState([]);

  // fetch data from backend
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/transactions/all")
      .then((response) => {
        setApplications(response.data);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
      });
  }, []);

  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>

      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Credit Limit</th>
                <th>Approval Date</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.id}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.phone}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.product}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.creditLimit}</td>
                  <td>{app.approvalDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}

export default SalesApplicationsTable;
//css code
.sc-container {
    padding: 20px;
    background: #f5faff;
    min-height: 100vh;
  }
 
  .sc-title {
    text-align: center;
    color: #0066a1; /* Standard Chartered Blue */
    margin-bottom: 20px;
  }
 
  .sc-loading,
  .sc-error,
  .sc-empty {
    text-align: center;
    font-size: 1.1rem;
    color: #0066a1;
  }
 
  .sc-error {
    color: #d9534f;
  }
 
  .sc-table-wrapper {
    overflow-x: auto;
  }
 
  .sc-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    box-shadow: 0px 2px 8px rgba(0, 102, 161, 0.15);
  }
 
  .sc-table th {
    background: #0066a1; /* Blue Header */
    color: white;
    padding: 10px;
    text-align: left;
  }
 
  .sc-table td {
    padding: 10px;
    border-bottom: 1px solid #d0e6f4;
  }
 
  .sc-table tr:nth-child(even) {
    background: #e6f5f1; /* Light Green Row */
  }
 
  .sc-table tr:hover {
    background: #c1e4dc; /* Highlight on Hover */
  }
 
  /* Status Colors */
  .status.approved {
    color: #1b8a5a; /* Green */
    font-weight: bold;
  }
 
  .status.rejected {
    color: #d9534f; /* Red */
    font-weight: bold;
  }
 
  .status.pending {
    color: #0066a1; /* Blue */
    font-weight: bold;
  }

INSERT INTO transactions 
(customer_id, name, dob, email, phone, credit_score, product, validity_period, credit_limit, status, approval_date, processed_by, application_timeline)
VALUES
(1001, 'Abhishek Kale', '2001-08-12', 'abhishek@example.com', '+91-9876543210', 782, 'Platinum Rewards Card', '2025-2030', '₹2,00,000', 'Approved', '2025-09-02', 'System Auto-Decision', 'Application Submitted'),

(1002, 'Riya Sharma', '1998-05-21', 'riya@example.com', '+91-9988776655', 810, 'Gold Credit Card', '2025-2030', '₹1,20,000', 'Pending', '2025-09-03', 'Sales User1', 'Application Processing'),

(1003, 'Arjun Mehta', '1995-02-15', 'arjun@example.com', '+91-9123456789', 650, 'Basic Cashback Card', '2025-2030', '₹60,000', 'Rejected', '2025-09-01', 'Sales User2', 'Credit Card Offered');

UPDATE transactions
SET processed_by = 'System Auto-Decision',
    application_timeline = 'Application Submitted'
WHERE processed_by IS NULL OR application_timeline IS NULL;

import React, { useState } from "react";
import "./SalesApplicationTable.css";
 
function SalesApplicationsTable() {
  // 🔹 Fixed timeline options
  const timelineStages = [
    "Application Submitted",
    "Application Processing",
    "Credit Card Offered",
    "Application Accepted",
    "Printed",
    "Shipped",
  ];
 
  // 🔹 Demo Data
  const [applications, setApplications] = useState([
    {
      applicationId: "APP1001",
      name: "Abhishek Kale",
      dob: "2001-08-12",
      email: "abhishek@example.com",
      contactNumber: "+91-9876543210",
      employmentType: "Salaried",
      monthlyIncome: "₹75,000",
      creditScore: 782,
      productType: "Platinum Rewards Card",
      status: "Approved",
      assignedLimit: "₹2,00,000",
      cardLast4: "5678",
      approvalDate: "2025-09-02",
      processedBy: "System Auto-Decision",
      applicationTimeline: "Application Submitted",
    },
    {
      applicationId: "APP1002",
      name: "Riya Sharma",
      dob: "1998-05-21",
      panNumber: "RIYSH1234K",
      email: "riya@example.com",
      contactNumber: "+91-9988776655",
      employmentType: "Self-Employed",
      monthlyIncome: "₹1,20,000",
      creditScore: 810,
      productType: "Gold Travel Card",
      status: "Pending",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "-",
      processedBy: "Underwriter01",
      applicationTimeline: "Application Processing",
    },
    {
      applicationId: "APP1003",
      name: "Arjun Mehta",
      dob: "1995-02-15",
      panNumber: "ARJMH9876L",
      email: "arjun@example.com",
      contactNumber: "+91-9123456789",
      employmentType: "Salaried",
      monthlyIncome: "₹60,000",
      creditScore: 650,
      productType: "Basic Cashback Card",
      status: "Rejected",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "2025-09-01",
      processedBy: "Underwriter02",
      applicationTimeline: "Credit Card Offered",
    },
  ]);
 
  // 🔹 Handle timeline change
  const handleTimelineChange = (id, newTimeline) => {
    setApplications((prevApps) =>
      prevApps.map((app) =>
        app.applicationId === id ? { ...app, applicationTimeline: newTimeline } : app
      )
    );
  };
 
  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>
 
      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>Application ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Assigned Limit</th>
                <th>Approval Date</th>
                <th>Processed By</th>
                <th>Application Timeline</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.applicationId}>
                  <td>{app.applicationId}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.contactNumber}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.productType}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.assignedLimit}</td>
                  <td>{app.approvalDate}</td>
                  <td>{app.processedBy}</td>
                  <td>
                    <select
                      value={app.applicationTimeline}
                      onChange={(e) =>
                        handleTimelineChange(app.applicationId, e.target.value)
                      }
                    >
                      {timelineStages.map((stage, index) => (
                        <option key={index} value={stage}>
                          {stage}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}
 
export default SalesApplicationsTable;
import React, { useState } from "react";
import "./SalesApplicationTable.css";
 
function SalesApplicationsTable() {
  // 🔹 Fixed timeline options
  const timelineStages = [
    "Application Submitted",
    "Application Processing",
    "Credit Card Offered",
    "Application Accepted",
    "Printed",
    "Shipped",
  ];
 
  // 🔹 Demo Data
  const [applications, setApplications] = useState([
    {
      applicationId: "APP1001",
      name: "Abhishek Kale",
      dob: "2001-08-12",
      email: "abhishek@example.com",
      contactNumber: "+91-9876543210",
      employmentType: "Salaried",
      monthlyIncome: "₹75,000",
      creditScore: 782,
      productType: "Platinum Rewards Card",
      status: "Approved",
      assignedLimit: "₹2,00,000",
      cardLast4: "5678",
      approvalDate: "2025-09-02",
      processedBy: "System Auto-Decision",
      applicationTimeline: "Application Submitted",
    },
    {
      applicationId: "APP1002",
      name: "Riya Sharma",
      dob: "1998-05-21",
      panNumber: "RIYSH1234K",
      email: "riya@example.com",
      contactNumber: "+91-9988776655",
      employmentType: "Self-Employed",
      monthlyIncome: "₹1,20,000",
      creditScore: 810,
      productType: "Gold Travel Card",
      status: "Pending",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "-",
      processedBy: "Underwriter01",
      applicationTimeline: "Application Processing",
    },
    {
      applicationId: "APP1003",
      name: "Arjun Mehta",
      dob: "1995-02-15",
      panNumber: "ARJMH9876L",
      email: "arjun@example.com",
      contactNumber: "+91-9123456789",
      employmentType: "Salaried",
      monthlyIncome: "₹60,000",
      creditScore: 650,
      productType: "Basic Cashback Card",
      status: "Rejected",
      assignedLimit: "-",
      cardLast4: "-",
      approvalDate: "2025-09-01",
      processedBy: "Underwriter02",
      applicationTimeline: "Credit Card Offered",
    },
  ]);
 
  // 🔹 Handle timeline change
  const handleTimelineChange = (id, newTimeline) => {
    setApplications((prevApps) =>
      prevApps.map((app) =>
        app.applicationId === id ? { ...app, applicationTimeline: newTimeline } : app
      )
    );
  };
 
  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>
 
      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>Application ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Assigned Limit</th>
                <th>Approval Date</th>
                <th>Processed By</th>
                <th>Application Timeline</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.applicationId}>
                  <td>{app.applicationId}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.contactNumber}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.productType}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.assignedLimit}</td>
                  <td>{app.approvalDate}</td>
                  <td>{app.processedBy}</td>
                  <td>
                    <select
                      value={app.applicationTimeline}
                      onChange={(e) =>
                        handleTimelineChange(app.applicationId, e.target.value)
                      }
                    >
                      {timelineStages.map((stage, index) => (
                        <option key={index} value={stage}>
                          {stage}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}
 
export default SalesApplicationsTable;

import React, { useState, useEffect } from "react";
import axios from "axios";
import "./SalesApplicationTable.css";

function SalesApplicationsTable() {
  const [applications, setApplications] = useState([]);

  // fetch data from backend
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/transactions/all")
      .then((response) => {
        setApplications(response.data);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
      });
  }, []);

  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>

      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Credit Limit</th>
                <th>Approval Date</th>
                <th>Processed By</th>
                <th>Application Timeline</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.id}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.phone}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.product}</td>
                  <td className={`status ${app.status.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.creditLimit}</td>
                  <td>{app.approvalDate}</td>
                  <td>
                    <select
                      value={app.applicationTimeline}
                      onChange={(e) =>
                        handleTimelineChange(app.applicationId, e.target.value)
                      }
                    >
                      {timelineStages.map((stage, index) => (
                        <option key={index} value={stage}>
                          {stage}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}

export default SalesApplicationsTable;


import React, { useState, useEffect } from "react";
import axios from "axios";
import "./SalesApplicationTable.css";

function SalesApplicationsTable() {
  const [applications, setApplications] = useState([]);

  // 🔹 Fixed timeline options
  const timelineStages = [
    "Application Submitted",
    "Application Processing",
    "Credit Card Offered",
    "Application Accepted",
    "Printed",
    "Shipped",
  ];

  // 🔹 Fetch data from backend
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/transactions/all")
      .then((response) => {
        setApplications(response.data);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
      });
  }, []);

  // 🔹 Handle timeline change (frontend only for now)
  const handleTimelineChange = (id, newTimeline) => {
    setApplications((prevApps) =>
      prevApps.map((app) =>
        app.id === id ? { ...app, applicationTimeline: newTimeline } : app
      )
    );

    // (Optional) Send update to backend when timeline changes
    axios
      .put(`http://localhost:8080/api/transactions/${id}/timeline`, {
        applicationTimeline: newTimeline,
      })
      .catch((error) => {
        console.error("Error updating timeline:", error);
      });
  };

  return (
    <div className="sc-container">
      <h2 className="sc-title">Customer Credit Card Applications</h2>

      {applications.length > 0 ? (
        <div className="sc-table-wrapper">
          <table className="sc-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Customer Name</th>
                <th>DOB</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Credit Score</th>
                <th>Product</th>
                <th>Status</th>
                <th>Credit Limit</th>
                <th>Approval Date</th>
                <th>Processed By</th>
                <th>Application Timeline</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.id}</td>
                  <td>{app.name}</td>
                  <td>{app.dob}</td>
                  <td>{app.email}</td>
                  <td>{app.phone}</td>
                  <td>{app.creditScore}</td>
                  <td>{app.product}</td>
                  <td className={`status ${app.status?.toLowerCase()}`}>
                    {app.status}
                  </td>
                  <td>{app.creditLimit}</td>
                  <td>{app.approvalDate}</td>
                  <td>{app.processedBy}</td>
                  <td>
                    <select
                      value={app.applicationTimeline || "Application Submitted"}
                      onChange={(e) =>
                        handleTimelineChange(app.id, e.target.value)
                      }
                    >
                      {timelineStages.map((stage, index) => (
                        <option key={index} value={stage}>
                          {stage}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p className="sc-empty">No applications found.</p>
      )}
    </div>
  );
}

export default SalesApplicationsTable;


