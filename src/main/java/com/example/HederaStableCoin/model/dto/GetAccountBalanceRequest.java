package com.example.HederaStableCoin.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetAccountBalanceRequest {
    private String tokenId;
    private String targetId;
}
