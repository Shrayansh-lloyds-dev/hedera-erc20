package com.example.HederaStableCoin.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StableCoinViewModel {
    private String tokenId;
    private String name;
    private String symbol;
    private int decimals;
    private long initialSupply;
    private String treasuryAccountId;
}