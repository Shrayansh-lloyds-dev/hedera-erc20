package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceResponse {
    private int statusCode;
    private String message;
    private String tokenId; // or null for HBAR
    private String accountId;
    private BigDecimal value;
    private int decimals;
}