package com.example.HederaStableCoin.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MultiSigTransactionViewModel {
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private long amount;
    private String status;
    private Instant createdAt;
    private boolean Hbar;
    private String tokenId;
}