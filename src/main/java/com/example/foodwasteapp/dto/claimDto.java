package com.example.foodwasteapp.dto;
import java.time.LocalDateTime;

public class claimDto {
    private Long id;
    private Long requesterID;
    private Long listingID;
    private String status;
    private LocalDateTime requestedAt;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequesterID() {
        return requesterID;
    }
    public void setRequesterID(Long requesterID) {
        this.requesterID = requesterID;
    }

    public Long getListingID() {
        return listingID;
    }
    public void setListingID(Long listingID) {
        this.listingID = listingID;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }
    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}

