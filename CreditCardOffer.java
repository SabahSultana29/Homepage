package com.scb.creditcardorigination.userStory6.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Entity
@Table(name = "credit_card_offers")
public class CreditCardOffer {
    public long getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(long offer_id) {
        this.offer_id = offer_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long offer_id;
    private String offer_name;
    private String description;
    private double annual_fee;

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
    public CreditCardOffer(){}

    public LocalDateTime getCreditLimit() {

        return null;
    }
    public String getCardNumber() {
        return null;
    }


    public void setStatus(String accepted) {
    }
}
