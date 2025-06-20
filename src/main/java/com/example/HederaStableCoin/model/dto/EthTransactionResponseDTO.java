package com.example.HederaStableCoin.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EthTransactionResponseDTO {
    private String transactionHash;
    private String status;
    private String blockHash;
    private String blockNumber;
    private String from;
    private String to;
    private String gasUsed;
    private String error;
}
