package com.example.HederaStableCoin.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stablecoins")
public class StablecoinEntity {
    @Id
    private String tokenId;
    private String name;
    private String symbol;
    private int decimals;
    private long initialSupply;
    private String treasuryAccountId;
    private String treasuryAccountPrivateKey;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    public String getTreasuryAccountId() {
        return treasuryAccountId;
    }

    public void setTreasuryAccountId(String treasuryAccountId) {
        this.treasuryAccountId = treasuryAccountId;
    }

    public String getTreasuryAccountPrivateKey() {
        return treasuryAccountPrivateKey;
    }

    public void setTreasuryAccountPrivateKey(String treasuryAccountPrivateKey) {
        this.treasuryAccountPrivateKey = treasuryAccountPrivateKey;
    }
}