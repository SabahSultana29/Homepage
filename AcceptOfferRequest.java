package com.scb.creditcardorigination.userStory6.dto;

public class AcceptOfferRequest {
    private long offer_id;
    private String offer_name;
    private String description;
    private double annual_fee;

    public AcceptOfferRequest(long offer_id, String offer_name, String description, double annual_fee) {
        this.offer_id = offer_id;
        this.offer_name = offer_name;
        this.description = description;
        this.annual_fee = annual_fee;
    }

    public long getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(long offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAnnual_fee() {
        return annual_fee;
    }

    public void setAnnual_fee(double annual_fee) {
        this.annual_fee = annual_fee;
    }

    public Long getOfferId() {
        return offer_id;
    }

}