package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.StablecoinDetails;
import com.hedera.hashgraph.sdk.TokenId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryStablecoinStore {
    private final Map<TokenId, StablecoinDetails> stablecoinMap = new HashMap<>();

    public void saveStablecoin(TokenId tokenId, StablecoinDetails stablecoinDetails) {
        stablecoinMap.put(tokenId, stablecoinDetails);
    }

    public boolean exists(TokenId tokenId) {
        return stablecoinMap.containsKey(tokenId);
    }

    public Set<TokenId> getAllTokenIds() {
        return stablecoinMap.keySet();
    }

    public StablecoinDetails getStablecoin(TokenId tokenId) {
        return stablecoinMap.get(tokenId);
    }
}
