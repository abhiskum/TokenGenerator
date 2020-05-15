package org.gs.token.service;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.gs.token.model.ItemType;
import org.gs.token.model.Token;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Token service to generate token and list tokens
 */
@Singleton
public class TokenService {

    private Map<String, Map<String, ItemType>> itemTypesByClientId;

    /**
     * Read client information from properties file and store in local cache
     */
    public TokenService() {

        Config config = ConfigProvider.getConfig();
        String clients = config.getValue("clients", String.class);
        this.itemTypesByClientId = new HashMap<>();

        for (String client : clients.split(",")) {
            String items = config.getValue(client + ".items", String.class);
            for (String item : items.split(",")) {
                String uiFields = config.getValue(client + ".item." + item + ".ui.fields", String.class);
                Optional<String> displayName = config
                    .getOptionalValue(client + ".item." + item + ".display.name", String.class);
                String slotDuration = config
                    .getValue(client + ".item." + item + ".slot.duration", String.class);
                String personPerSlot = config
                    .getValue(client + ".item." + item + ".person.per.slot", String.class);
                String tokenGenerationAfter = config
                    .getValue(client + ".item." + item + ".token.generation.after", String.class);
                String tokenDays = config.getValue(client + ".item." + item + ".token.days", String.class);
                String tokenStartTime = config
                    .getValue(client + ".item." + item + ".token.start.time", String.class);
                String sellDays = config.getValue(client + ".item." + item + ".sell.days", String.class);
                String sellStartTime = config
                    .getValue(client + ".item." + item + ".sell.start.time", String.class);
                String sellEndTime = config
                    .getValue(client + ".item." + item + ".sell.end.time", String.class);

                Map<String, ItemType> itemTypeMap = this.itemTypesByClientId.get(client);
                if (itemTypeMap == null) {
                    itemTypeMap = new HashMap<>();
                    this.itemTypesByClientId.put(client, itemTypeMap);
                }
                itemTypeMap.put(item, new ItemType(UUID.randomUUID().toString(), client, item,
                    Arrays.asList(uiFields.split(",")), displayName.isPresent() ? displayName.get() : "",
                    Integer.parseInt(slotDuration), Integer.parseInt(personPerSlot),
                    Integer.parseInt(tokenGenerationAfter), Arrays.asList(tokenDays.split(",")), tokenStartTime,
                    Arrays.asList(sellDays.split(",")), sellStartTime, sellEndTime));
            }
        }
    }

    /**
     * @param clientId
     * @param requestDetails
     * @return Token
     */
    public Token getToken(String clientId, MultivaluedMap<String, String> requestDetails) {
        ItemType itemType = getItemType(clientId, requestDetails.getFirst("Item"));
        Token token = itemType.getToken(requestDetails);
        return token;
    }

    /**
     * @param clientId
     * @param item
     * @return ItemType
     */
    public ItemType getItemType(String clientId, String item) {
        Map<String, ItemType> itemTypeMap = this.itemTypesByClientId.get(clientId);
        if (itemTypeMap == null) {
            throw new RuntimeException(String.format("%s client is not registered", clientId));
        }
        ItemType itemType = itemTypeMap.get(item);
        if (itemType == null) {
            throw new RuntimeException(String.format("%s is invalid token type", itemType));
        }
        return itemType;
    }

    public Map<Integer, String> getItemSlots(String clientId, String item) {
        ItemType itemType = getItemType(clientId, item);
        return itemType.getItemSlotTimings();
    }

    /**
     * @param clientId
     * @return all items
     */
    public Collection<ItemType> getItemTypes(String clientId) {
        Map<String, ItemType> itemTypeMap = this.itemTypesByClientId.get(clientId);
        if (itemTypeMap == null) {
            throw new RuntimeException(String.format("%s client is not registered", clientId));
        }
        return itemTypeMap.values();
    }

}
