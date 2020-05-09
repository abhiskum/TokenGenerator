package org.ggs.token.service;

import org.ggs.token.model.Token;
import org.ggs.token.model.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenService {

    private Map<String, Map<String, TokenType>> tokenTypeMap;
    public TokenService() {

        this.tokenTypeMap = new HashMap<>();

        List<TokenType> tokenTypes = Arrays.asList(
            new TokenType(UUID.randomUUID().toString(), "gateway", "vegetable", "Vegetable", 15, 8, 10, 17),
            new TokenType(UUID.randomUUID().toString(), "gateway", "fruit", "Fruit", 10, 9, 12, 13),
            new TokenType(UUID.randomUUID().toString(), "greencounty", "vegetable", "Vegetable", 15, 8, 10,
                17));

        tokenTypes.stream().forEach(tokenType -> {
            Map<String, TokenType> tokenTypeMap = this.tokenTypeMap.get(tokenType.getParentName());
            if (tokenTypeMap == null) {
                tokenTypeMap = new HashMap<>();
                this.tokenTypeMap.put(tokenType.getParentName(), tokenTypeMap);
            }
            tokenTypeMap.put(tokenType.getName(), tokenType);
        });
    }

    public Token getToken(String parentName, String tokenName) {
        TokenType tokenType = getTokenType(parentName, tokenName);
        Token token = tokenType.getToken();
        return token;
    }

    public List<Token> getAllTokens(String parentName, String tokenName) {
        TokenType tokenType = getTokenType(parentName, tokenName);
        return tokenType.getGeneratedTokens();
    }

    public TokenType getTokenType(String parentName, String tokenName) {
        Map<String, TokenType> tokenTypeMap = this.tokenTypeMap.get(parentName);
        if (tokenTypeMap == null) {
            throw new RuntimeException(String.format("%s is not registered", parentName));
        }
        TokenType tokenType = tokenTypeMap.get(tokenName);
        if (tokenType == null) {
            throw new RuntimeException(String.format("%s is invalid token type", tokenName));
        }
        return tokenType;
    }

}
