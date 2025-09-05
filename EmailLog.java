package com.scb.creditcardorigination.userStory6.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_log")
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String emailId;

    private String cardNumber;
    @Column(length = 1000)
    private String message;

    private LocalDateTime sentAt;
    public EmailLog() {}

    public EmailLog(String emailId, String cardNumber, String message, LocalDateTime sentAt) {
        this.emailId = emailId;
        this.cardNumber = cardNumber;
        this.message = message;
        this.sentAt = sentAt;
    }


    public Long getId() {
        return id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
                ", EmailId=" + emailId +
                ", cardNumber='" + cardNumber + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}