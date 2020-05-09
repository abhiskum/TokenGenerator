package org.ggs.token.model;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TokenType {

    private String uuid;
    private String parentName;
    private String name;
    private String displayName;
    private Integer currentValue;
    private Integer processingDuration;
    private Integer tokenStartTime;
    private Integer sellStartTime;
    private Integer sellEndTime;
    private Date estimatedTime;
    private List<Token> generatedTokens;

    public TokenType() {
    }

    public TokenType(String uuid, String parentName, String name, String displayName,
        Integer processingDuration, Integer tokenStartTime, Integer sellStartTime, Integer sellEndTime) {
        this.uuid = uuid;
        this.parentName = parentName;
        this.name = name;
        this.displayName = displayName;
        this.processingDuration = processingDuration;
        this.tokenStartTime = tokenStartTime;
        this.sellStartTime = sellStartTime;
        this.sellEndTime = sellEndTime;
        this.currentValue = 1;
        this.estimatedTime = getSellStartDateTime();
        this.generatedTokens = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public TokenType setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getParentName() {
        return parentName;
    }

    public TokenType setParentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public String getName() {
        return name;
    }

    public TokenType setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TokenType setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public TokenType setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public Integer getProcessingDuration() {
        return processingDuration;
    }

    public TokenType setProcessingDuration(Integer processingDuration) {
        this.processingDuration = processingDuration;
        return this;
    }

    public Integer getTokenStartTime() {
        return tokenStartTime;
    }

    public TokenType setTokenStartTime(Integer tokenStartTime) {
        this.tokenStartTime = tokenStartTime;
        return this;
    }

    public Integer getSellStartTime() {
        return sellStartTime;
    }

    public TokenType setSellStartTime(Integer sellStartTime) {
        this.sellStartTime = sellStartTime;
        return this;
    }

    public Integer getSellEndTime() {
        return sellEndTime;
    }

    public TokenType setSellEndTime(Integer sellEndTime) {
        this.sellEndTime = sellEndTime;
        return this;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public TokenType setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
        return this;
    }

    public List<Token> getGeneratedTokens() {
        return generatedTokens;
    }

    public synchronized Token getToken() {
        Date tokenStartDateTime = getTokenStartDateTime();
        Date currentDateTime = new Date();
        if (currentDateTime.before(tokenStartDateTime)) {
            throw new RuntimeException("Token generation start at " + tokenStartDateTime);
        }
        if (!DateUtils.isSameDay(tokenStartDateTime, currentDateTime)) {
            this.currentValue = 1;
            this.estimatedTime = getSellStartDateTime();
            this.generatedTokens.clear();
        }
      /*  if (this.estimatedTime.after(getSellEndDateTime())) {
            throw new RuntimeException("No slot available");
        }*/
        Token token = new Token(currentValue, this, estimatedTime, new Date());
        this.generatedTokens.add(token);
        this.currentValue = this.currentValue + 1;
        this.estimatedTime = DateUtils.addMinutes(this.estimatedTime, processingDuration);
        return token;
    }

    private Date getTokenStartDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, tokenStartTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getSellStartDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, sellStartTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getSellEndDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, sellEndTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public String toString() {
        return "TokenType{" + "uuid='" + uuid + '\'' + ", parentName='" + parentName + '\'' + ", name='" +
            name + '\'' + '}';
    }
}
