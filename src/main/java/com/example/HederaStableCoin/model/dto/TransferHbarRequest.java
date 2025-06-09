package com.example.HederaStableCoin.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferHbarRequest {
    private String fromAccountId;
    private String fromPrivateKey;
    private String toAccountId;
    private long amount;
}