package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Balance {
    private BigDecimal value;
    private int decimals;
    private int statusCode;
    private String message;

}
