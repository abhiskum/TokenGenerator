package org.ggs.token.model;

import org.ggs.token.model.TokenType;

import java.util.Date;

public class Token {

    private Integer tokenNumber;
    private TokenType tokenType;
    private Date estimatedDate;
    private Date createdDate;

    public Token(Integer tokenNumber, TokenType tokenType, Date estimatedDate, Date createdDate) {
        this.tokenNumber = tokenNumber;
        this.tokenType = tokenType;
        this.estimatedDate = estimatedDate;
        this.createdDate = createdDate;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public Token setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
        return this;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Token setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public Date getEstimatedDate() {
        return estimatedDate;
    }

    public Token setEstimatedDate(Date estimatedDate) {
        this.estimatedDate = estimatedDate;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Token setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public String toString() {
        return "Token{" + "tokenNumber=" + tokenNumber + ", tokenType=" + tokenType + ", estimatedDate=" +
            estimatedDate + ", createdDate=" + createdDate + '}';
    }
}
