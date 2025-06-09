package com.example.HederaStableCoin.model.dto;

import com.hedera.hashgraph.sdk.TransactionReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferHbarResponse {
    private int statusCode;
    private String message;
    private String fromAccountId;
    private String toAccountId;
    private String transactionId;
    private TransactionReceipt receipt;

}