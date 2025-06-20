package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EthTransactionDetailsDTO {
    private String transactionHash;
    private String from;
    private String to;
    private String value;
    private String blockHash;
    private String blockNumber;
    private String gas;
    private String gasPrice;
    private String status;
    private String gasUsed;
    private String contractAddress;
    private String error;
}