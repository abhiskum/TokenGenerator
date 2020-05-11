package org.gs.token.model;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemType {

    private String uuid;
    private String clientId;
    private String name;
    private String displayName;
    private Integer currentValue;
    private Integer slotDuration;
    private Integer personPerSlot;
    private String tokenStartTime;
    private String sellStartTime;
    private String sellEndTime;

    private Date currentSlotStartTiming;
    private Date currentSlotEndTiming;
    private Integer currentPersonPerSlot;
    private List<Token> generatedTokens;

    /**
     * Default constructor
     */
    public ItemType() {
    }

    /**
     * @param uuid
     * @param clientId
     * @param name
     * @param displayName
     * @param slotDuration
     * @param personPerSlot
     * @param tokenStartTime
     * @param sellStartTime
     * @param sellEndTime
     */
    public ItemType(String uuid, String clientId, String name, String displayName, Integer slotDuration,
        Integer personPerSlot, String tokenStartTime, String sellStartTime, String sellEndTime) {
        this.uuid = uuid;
        this.clientId = clientId;
        this.name = name;
        this.displayName = displayName;
        this.slotDuration = slotDuration;
        this.personPerSlot = personPerSlot;
        this.tokenStartTime = tokenStartTime;
        this.sellStartTime = sellStartTime;
        this.sellEndTime = sellEndTime;
        this.currentValue = 1;

        this.currentSlotStartTiming = getNextSlotStartTime();
        this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
        this.currentPersonPerSlot = 0;
        this.generatedTokens = new ArrayList<>();
    }

    /**
     * @return token type display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return list of generated tokens
     */
    public List<Token> getGeneratedTokens() {
        return generatedTokens;
    }

    /**
     * Generate token
     *
     * @param tokenRequest
     * @return generated token
     */
    public synchronized Token getToken(TokenRequest tokenRequest) {
        Date currentDateTime = new Date();

        Date tokenStartDateTime = getDateTime(tokenStartTime);
        if (currentDateTime.before(tokenStartDateTime)) {
            throw new RuntimeException("Token generation start at " + tokenStartDateTime);
        }
        if (!DateUtils.isSameDay(tokenStartDateTime, currentDateTime)) {
            this.currentValue = 1;
            this.currentSlotStartTiming = getNextSlotStartTime();
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentPersonPerSlot = 0;
            this.generatedTokens.clear();
        }

        if (currentDateTime.after(this.currentSlotEndTiming)) {
            this.currentSlotStartTiming = getNextSlotStartTime();
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentPersonPerSlot = 0;
        }

        if (this.currentPersonPerSlot.equals(personPerSlot)) {
            this.currentSlotStartTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
        }

        Token token = new Token(currentValue, this, currentSlotStartTiming, currentSlotEndTiming, new Date(),
            tokenRequest.getRequesterName(), tokenRequest.getRequesterFlatNumber(),
            tokenRequest.getRequesterMobile());
        this.generatedTokens.add(token);
        this.currentValue = this.currentValue + 1;
        this.currentPersonPerSlot = this.currentPersonPerSlot + 1;
        return token;
    }

    /**
     * @return Set time string into calender object and return date.
     */
    private Date getDateTime(String timeStr) {
        Calendar calendar = Calendar.getInstance();
        String[] split = timeStr.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
        calendar.set(Calendar.MINUTE, split.length > 1 ? Integer.parseInt(split[1]) : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @return next slot start time
     */
    private Date getNextSlotStartTime() {
        Date currentDateTime = new Date();
        Date slotStartDateTime = getDateTime(sellStartTime);
        while (slotStartDateTime.before(currentDateTime)) {
            slotStartDateTime = DateUtils.addMinutes(slotStartDateTime, slotDuration);
        }
        return slotStartDateTime;
    }

    @Override
    public String toString() {
        return "ItemType{" + "uuid='" + uuid + '\'' + ", parentName='" + clientId + '\'' + ", name='" + name +
            '\'' + '}';
    }
}
