package com.example.HederaStableCoin.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStableCoinDetailsRequest {
    private String id;

    public GetStableCoinDetailsRequest() {
    }

    public GetStableCoinDetailsRequest(String id) {
        this.id = id;
    }
}
