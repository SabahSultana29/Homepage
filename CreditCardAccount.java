package com.scb.creditcardorigination.userStory6.model;
import jakarta.persistence.*;

import java.nio.channels.AcceptPendingException;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_card_accounts")
public class CreditCardAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  String emailId;
    private String cardNumber;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private CreditCardOffer offer;

    public CreditCardAccount() {
    }

    public CreditCardAccount(String cardNumber, LocalDateTime createdAt, Customer customer, CreditCardOffer offer) {
        this.cardNumber = cardNumber;
        this.createdAt = createdAt;
        this.customer = customer;
        this.offer = offer;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CreditCardOffer getOffer() {
        return offer;
    }

    public void setOffer(CreditCardOffer offer) {
        this.offer = offer;
    }

    public void setStatus(String active) {
    }

    public void setCreditLimit(LocalDateTime creditLimit) {
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return  emailId;
    }
}
