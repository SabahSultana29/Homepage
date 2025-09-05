package com.scb.creditcardorigination.userStory6.controller;
import com.scb.creditcardorigination.userStory6.dto.EmailRequest;
import com.scb.creditcardorigination.userStory6.model.EmailLog;
import com.scb.creditcardorigination.userStory6.repository.EmailLogRepository;
import com.scb.creditcardorigination.userStory6.service.EmailService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email_logs")
public class EmailLogController {
    private final EmailLogRepository emailLogRepository;
    private final EmailService emailService;
    public EmailLogController(EmailLogRepository emailLogRepository, EmailService emailService) {
        this.emailLogRepository = emailLogRepository;
        this.emailService = emailService;
    }
    // GET all logs
    @GetMapping
    public List<EmailLog> getAllLogs() {
        return emailLogRepository.findAll();
    }

    @PostMapping("/send")
    public Map<String, String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendConfirmation(request.getEmailId(), request.getCardNumber());

        Map<String, String> response = new HashMap<>();
        response.put("status", "Email Sent Successfully");
        response.put("EmailId", String.valueOf(request.getEmailId()));
        response.put("cardNumber", request.getCardNumber());
        return response;
    }
}




