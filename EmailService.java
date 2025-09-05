package com.scb.creditcardorigination.userStory6.service;
import com.scb.creditcardorigination.userStory6.model.EmailLog;
import com.scb.creditcardorigination.userStory6.repository.EmailLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Service
public class EmailService {
    private final EmailLogRepository emailLogRepository;
    public EmailService(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }
    public void sendConfirmation(String emailId, String cardNumber) {
        // Save email log entry
        EmailLog emailLog = new EmailLog();
        emailLog.setEmailId(emailId);
        emailLog.setCardNumber(cardNumber);
        // Add subject + body
        String subject = "Credit Card Creation Confirmation";
        String body = "Dear Customer,\n\nYour credit card with number " + cardNumber +
                " has been created successfully.\n\nThank you for choosing our services.\n\nRegards,\nSCB Team";

        emailLog.setMessage("Subject: " + subject + "\n\nBody:\n" + body);
        emailLog.setSentAt(LocalDateTime.now());
        emailLogRepository.save(emailLog);
        // Print formatted email in console
        System.out.println("===== EMAIL SENT =====");
        System.out.println("To : " + emailId);
        System.out.println("Card Number: " + cardNumber);
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
    }

}

