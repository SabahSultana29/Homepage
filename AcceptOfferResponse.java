package com.scb.creditcardorigination.userStory6.dto;


public class AcceptOfferResponse {
    private String status;
    private String message;
    private String cardNumber;

    public AcceptOfferResponse(boolean b, String message, String cardNumber) {
        this.status = b ? "Success" : "Failure";
        this.message = message;
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}

