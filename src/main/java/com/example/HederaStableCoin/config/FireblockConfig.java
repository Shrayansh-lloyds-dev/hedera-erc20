package com.example.HederaStableCoin.config;

import com.fireblocks.sdk.BasePath;
import com.fireblocks.sdk.ConfigurationOptions;
import com.fireblocks.sdk.Fireblocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Paths;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class FireblockConfig {

    @Value("${fireblock.api.key}")
    private String apiKey;


    @Value("${fireblock.env}")
    private String fireblocksEnv;

    @Bean
    public Fireblocks fireblocksClient() throws IOException {
        String privateKeyPem = new String(
                Files.readAllBytes(
                        Paths.get("src/main/resources/fireblocks_secret.key")
                )
        );
        ConfigurationOptions opts = new ConfigurationOptions()
                .apiKey(apiKey)
                .secretKey(privateKeyPem)
                .basePath(BasePath.Sandbox);
        return new Fireblocks(opts);
    }
}