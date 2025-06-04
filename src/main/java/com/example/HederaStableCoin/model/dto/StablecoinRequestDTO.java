package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StablecoinRequestDTO {

    private String name;
    private String symbol;
    private long initialSupply;
    private int decimals;
    private String treasuryAccountId;
    private String treasuryPrivateKey;
}
