package org.gs.token.model;

import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;

public class Token {

    private Integer tokenNumber;
    private ItemType itemType;
    private Date slotStartTiming;
    private Date slotEndTiming;
    private Date createdDate;
    private MultivaluedMap<String, String> requestDetails;


    public Token(Integer tokenNumber, ItemType itemType, Date slotStartTiming, Date slotEndTiming,
        Date createdDate, MultivaluedMap<String, String> requestDetails) {
        this.tokenNumber = tokenNumber;
        this.itemType = itemType;
        this.slotStartTiming = slotStartTiming;
        this.slotEndTiming = slotEndTiming;
        this.createdDate = createdDate;
        this.requestDetails = requestDetails;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Date getSlotStartTiming() {
        return slotStartTiming;
    }

    public Date getSlotEndTiming() {
        return slotEndTiming;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public MultivaluedMap<String, String> getRequestDetails() {
        return requestDetails;
    }

    @Override
    public String toString() {
        return "Token{" + "tokenNumber=" + tokenNumber + ", itemType=" + itemType + ", slotStartTiming=" +
            slotStartTiming + ", slotEndTiming=" + slotEndTiming + ", requestDetails='" + requestDetails +
            '\'' + '}';
    }
}
