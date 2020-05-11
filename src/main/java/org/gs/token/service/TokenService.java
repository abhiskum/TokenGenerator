package org.gs.token.service;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.gs.token.model.ItemType;
import org.gs.token.model.Token;
import org.gs.token.model.TokenRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Singleton;

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
                String displayName = config
                    .getValue(client + ".item." + item + ".display.name", String.class);
                String slotDuration = config
                    .getValue(client + ".item." + item + ".slot.duration", String.class);
                String personPerSlot = config
                    .getValue(client + ".item." + item + ".person.per.slot", String.class);
                String tokenStartTime = config
                    .getValue(client + ".item." + item + ".token.start.time", String.class);
                String sellStartTime = config
                    .getValue(client + ".item." + item + ".sell.start.time", String.class);
                String sellEndTime = config
                    .getValue(client + ".item." + item + ".sell.end.time", String.class);

                Map<String, ItemType> itemTypeMap = this.itemTypesByClientId.get(client);
                if (itemTypeMap == null) {
                    itemTypeMap = new HashMap<>();
                    this.itemTypesByClientId.put(client, itemTypeMap);
                }
                itemTypeMap.put(item, new ItemType(UUID.randomUUID().toString(), client, item, displayName,
                    Integer.parseInt(slotDuration), Integer.parseInt(personPerSlot), tokenStartTime,
                    sellStartTime, sellEndTime));
            }
        }
    }

    /**
     * @param clientId
     * @param tokenRequest
     * @return Token
     */
    public Token getToken(String clientId, TokenRequest tokenRequest) {
        ItemType itemType = getItemType(clientId, tokenRequest.getItemType());
        Token token = itemType.getToken(tokenRequest);
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

}
