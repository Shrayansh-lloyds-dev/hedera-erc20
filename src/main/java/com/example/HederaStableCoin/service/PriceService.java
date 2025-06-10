package com.example.HederaStableCoin.service;

import com.example.HederaStableCoin.model.dto.PriceResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class PriceService {

    public PriceResponseDTO getPrices(String currency) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/simple/price")
                    .queryParam("ids", "bitcoin,ethereum,hedera-hashgraph")
                    .queryParam("vs_currencies", currency).toUriString();

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Map<String, Object>> response = restTemplate.getForObject(url, Map.class);

            double btc = extractDouble(response.get("bitcoin").get(currency.toLowerCase()));
            double eth = extractDouble(response.get("ethereum").get(currency.toLowerCase()));
            double hbar = extractDouble(response.get("hedera-hashgraph").get(currency.toLowerCase()));

            return new PriceResponseDTO(btc, eth, hbar);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch prices from CoinGecko API: " + e.getMessage(), e);
        }
    }

    private double extractDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unexpected type for price: " + (value != null ? value.getClass() : "null"));
        }
    }
}
