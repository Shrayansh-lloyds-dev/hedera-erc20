package com.example.HederaStableCoin.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssociateTokenRequest {
    private String accountId;
    private String privateKey;
    private String tokenId;
}
