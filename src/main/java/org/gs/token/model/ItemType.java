package org.gs.token.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

public class ItemType {

    private String uuid;
    private String clientId;
    private String name;
    private List<String> uiFields;
    private String displayName;
    private Integer currentValue;
    private Integer slotDuration;
    private Integer personPerSlot;
    Integer tokenGenerationAfter;
    List<String> tokenDays;
    private String tokenStartTime;
    List<String> sellDays;
    private String sellStartTime;
    private String sellEndTime;

    private Date currentSlotStartTiming;
    private Date currentSlotEndTiming;
    private Integer currentPersonPerSlot;
    private List<Token> generatedTokens;

    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

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
    public ItemType(String uuid, String clientId, String name, List<String> uiFields, String displayName,
        Integer slotDuration, Integer personPerSlot, Integer tokenGenerationAfter, List<String> tokenDays,
        String tokenStartTime,
        List<String> sellDays, String sellStartTime, String sellEndTime) {
        this.uuid = uuid;
        this.clientId = clientId;
        this.name = name;
        this.uiFields = uiFields;
        this.displayName = displayName;
        this.slotDuration = slotDuration;
        this.personPerSlot = personPerSlot;
        this.tokenGenerationAfter= tokenGenerationAfter;
        this.tokenDays = tokenDays;
        this.tokenStartTime = tokenStartTime;
        this.sellDays = sellDays;
        this.sellStartTime = sellStartTime;
        this.sellEndTime = sellEndTime;
        this.currentValue = 1;

        this.currentSlotStartTiming = getNextSlotStartTime();
        this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
        this.currentPersonPerSlot = 0;
        this.generatedTokens = new ArrayList<>();
    }

    /**
     * @return list of UI fields
     */
    public List<String> getUiFields() {
        return uiFields;
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
     * @param requestDetails
     * @return generated token
     */
    public synchronized Token getToken(MultivaluedMap<String, String> requestDetails) {
        Date currentDateTime = new Date();

        currentDateTime = DateUtils.addMinutes(currentDateTime, tokenGenerationAfter);

        if(!tokenDays.contains(dayFormat.format(currentDateTime))){
            throw new RuntimeException("Token generation days are " + String. join(",",
                tokenDays));
        }

        Date tokenStartDateTime = getDateTime(tokenStartTime);
        if (currentDateTime.before(tokenStartDateTime)) {
            throw new RuntimeException("Token generation start at " + tokenStartDateTime);
        }

        Date sellEndDataTime = getDateTime(sellEndTime);
        if (currentDateTime.after(sellEndDataTime)) {
            throw new RuntimeException("Token generation end at " + sellEndDataTime);
        }

        if (!DateUtils.isSameDay(tokenStartDateTime, currentDateTime)) {
            this.currentValue = 1;
            this.currentSlotStartTiming = getNextSlotStartTime();
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentPersonPerSlot = 0;
            this.generatedTokens.clear();
        }

        if (currentDateTime.after(this.currentSlotStartTiming)) {
            this.currentSlotStartTiming = getNextSlotStartTime();
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentPersonPerSlot = 0;
        }

        if (this.currentPersonPerSlot.equals(personPerSlot)) {
            this.currentSlotStartTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentSlotEndTiming = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
            this.currentPersonPerSlot = 0;
        }

        if (currentSlotStartTiming.after(sellEndDataTime)) {
            throw new RuntimeException("No slot available");
        }

        Token token = new Token(currentValue, this, currentSlotStartTiming, currentSlotEndTiming, new Date(),
            requestDetails);
        this.generatedTokens.add(token);
        this.currentValue = this.currentValue + 1;
        this.currentPersonPerSlot = this.currentPersonPerSlot + 1;
        return token;
    }

    /**
     * @return
     */
    public Map<Integer, String> getItemSlotTimings() {
        Map<Integer, String> slotTimings = new LinkedHashMap<>();
        Date nextSlotStartTime = getNextSlotStartTime();
        Date nextSlotEndTime = DateUtils.addMinutes(currentSlotStartTiming, slotDuration);
        Date sellEndDataTime = getDateTime(sellEndTime);
        while (nextSlotEndTime.before(sellEndDataTime)) {
            slotTimings.put(1, nextSlotStartTime.toString() + "-" + nextSlotEndTime);
            nextSlotStartTime = DateUtils.addMinutes(nextSlotStartTime, slotDuration);
            nextSlotEndTime = DateUtils.addMinutes(nextSlotStartTime, slotDuration);
        }

        return slotTimings;
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
