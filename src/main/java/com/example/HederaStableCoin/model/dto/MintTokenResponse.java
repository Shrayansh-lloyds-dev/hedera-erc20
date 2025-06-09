package com.example.HederaStableCoin.model.dto;

import com.hedera.hashgraph.sdk.TransactionReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MintTokenResponse {
    private int statusCode;
    private String message;
    private String tokenId;
    private long newTotalSupply;
    private TransactionReceipt receipt;
}