package org.gs.token.model;

import java.util.Date;

public class Token {

    private Integer tokenNumber;
    private ItemType itemType;
    private Date slotStartTiming;
    private Date slotEndTiming;
    private Date createdDate;
    private String requesterName;
    private String requesterMobile;
    private String requesterFlatNumber;

    public Token(Integer tokenNumber, ItemType itemType, Date slotStartTiming, Date slotEndTiming,
        Date createdDate, String requesterName, String requesterFlatNumber, String requesterMobile) {
        this.tokenNumber = tokenNumber;
        this.itemType = itemType;
        this.slotStartTiming = slotStartTiming;
        this.slotEndTiming = slotEndTiming;
        this.createdDate = createdDate;
        this.requesterName = requesterName;
        this.requesterMobile = requesterMobile;
        this.requesterFlatNumber = requesterFlatNumber;
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

    public String getRequesterName() {
        return requesterName;
    }

    public String getRequesterMobile() {
        return requesterMobile;
    }

    public String getRequesterFlatNumber() {
        return requesterFlatNumber;
    }

    @Override
    public String toString() {
        return "Token{" + "tokenNumber=" + tokenNumber + ", itemType=" + itemType + ", slotStartTiming=" +
            slotStartTiming + ", slotEndTiming=" + slotEndTiming + ", requesterName='" + requesterName +
            '\'' + '}';
    }
}
