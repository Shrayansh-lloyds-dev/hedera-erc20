package com.example.HederaStableCoin.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsAccountAssociatedTokenRequest {
    private String tokenId;
    private String targetId;
}