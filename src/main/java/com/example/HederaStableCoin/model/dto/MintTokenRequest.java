package com.example.HederaStableCoin.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MintTokenRequest {
    private String tokenId;
    private String treasuryAccountId;
    private String treasuryPrivateKey;
    private long amount;
}