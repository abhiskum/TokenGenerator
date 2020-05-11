package org.gs.token.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;

public class TokenRequest {

    @FormParam("name")
    @PartType("text/plain")
    private String requesterName;

    @FormParam("mobile")
    @PartType("text/plain")
    private String requesterMobile;

    @FormParam("flatNumber")
    @PartType("text/plain")
    private String requesterFlatNumber;

    @FormParam("type")
    @PartType("text/plain")
    private String itemType;

    public String getRequesterName() {
        return requesterName;
    }

    public TokenRequest setRequesterName(String requesterName) {
        this.requesterName = requesterName;
        return this;
    }

    public String getRequesterMobile() {
        return requesterMobile;
    }

    public TokenRequest setRequesterMobile(String requesterMobile) {
        this.requesterMobile = requesterMobile;
        return this;
    }

    public String getRequesterFlatNumber() {
        return requesterFlatNumber;
    }

    public TokenRequest setRequesterFlatNumber(String requesterFlatNumber) {
        this.requesterFlatNumber = requesterFlatNumber;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public TokenRequest setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }
}
