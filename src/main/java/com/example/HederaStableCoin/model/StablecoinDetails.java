package com.example.HederaStableCoin.model;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;

public class StablecoinDetails {

    private String name;
    private String symbol;
    private int decimals;
    private long initialSupply;
    private AccountId treasuryAccountId;
    private TokenId tokenId;

    public StablecoinDetails(String name, String symbol, int decimals, long initialSupply, AccountId treasuryAccountId, TokenId tokenId) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.initialSupply = initialSupply;
        this.treasuryAccountId = treasuryAccountId;
        this.tokenId = tokenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public long getInitialSupply() {
        return initialSupply;
    }

    public void setInitialSupply(long initialSupply) {
        this.initialSupply = initialSupply;
    }

    public AccountId getTreasuryAccountId() {
        return treasuryAccountId;
    }

    public void setTreasuryAccountId(AccountId treasuryAccountId) {
        this.treasuryAccountId = treasuryAccountId;
    }

    public TokenId getTokenId() {
        return tokenId;
    }

    public void setTokenId(TokenId tokenId) {
        this.tokenId = tokenId;
    }
}
