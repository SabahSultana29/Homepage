package com.scb.creditcardorigination.userStory6.dto;

public class EmailRequest {
    private String emailId;
    private String cardNumber;

    public EmailRequest() {
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
}



