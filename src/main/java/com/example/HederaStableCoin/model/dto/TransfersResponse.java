package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransfersResponse {
    private int statusCode;
    private String message;
    private String tokenId;
    private String fromAccountId;
    private Object receipt;
}