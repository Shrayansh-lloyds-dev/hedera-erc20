package com.example.HederaStableCoin.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StablecoinResponseDTO {

    private int statusCode;
    private String message;
    private String tokenId;
    private String tokenName;
    private String symbol;
    private Object receipt;
}
