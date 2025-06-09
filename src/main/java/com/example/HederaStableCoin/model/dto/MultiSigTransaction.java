package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultiSigTransaction {
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private long amount;
    private String status;
    private Instant createdAt;
}