package com.example.foodwasteapp.dto;
import java.time.LocalDateTime;

public class messageDto {
    private Long id;
    private Long senderID;
    private Long receiverID;
    private Long listingID;
    private String text;
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderID() {
        return senderID;
    }
    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getReceiverID() {
        return receiverID;
    }
    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public Long getListingID() {
        return listingID;
    }
    public void setListingID(Long listingID) {
        this.listingID = listingID;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
