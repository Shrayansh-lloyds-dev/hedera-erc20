package com.example.HederaStableCoin.config;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HederaClientConfig {

    @Value("${hedera.operator.account-id}")
    private String operatorId;

    @Value("${hedera.operator.private-key}")
    private String operatorKey;

    @Value("${hedera.network}")
    private String network;

    @Bean
    public Client hederaClient() {
        Client client = network.equalsIgnoreCase("mainnet") ? Client.forMainnet() : Client.forTestnet();

        client.setOperator(
            AccountId.fromString(operatorId),
            PrivateKey.fromString(operatorKey)
        );

        return client;
    }

    @Bean
    public PrivateKey operatorPrivateKey() {
        return PrivateKey.fromString(operatorKey);
    }
}
