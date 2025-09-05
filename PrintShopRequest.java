package com.scb.creditcardorigination.userStory6.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "printingShop_requests")
public class PrintShopRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private  String status;
    private String details;
    private LocalDateTime requestTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public  void setDetails(String details){
        this.details = details;
    }
    public  void setRequestTime(LocalDateTime requestTime){
        this.requestTime = requestTime;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }
}
