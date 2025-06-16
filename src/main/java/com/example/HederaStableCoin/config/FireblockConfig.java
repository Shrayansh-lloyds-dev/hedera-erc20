package com.example.HederaStableCoin.config;

import com.fireblocks.sdk.BasePath;
import com.fireblocks.sdk.ConfigurationOptions;
import com.fireblocks.sdk.Fireblocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FireblockConfig {

    @Value("${fireblock.api.key}")
    private String apiKey;

    @Value("${fireblock.api.secret}")
    private String secretKeyPath;

    @Value("${fireblock.env}")
    private String fireblocksEnv;

    @Bean
    public Fireblocks fireblocksClient() throws IOException {
        String secretKeyParam = Files.readString(Path.of(secretKeyPath));
        ConfigurationOptions opts = new ConfigurationOptions()
                .apiKey(apiKey)
                .secretKey(secretKeyParam)
                .basePath(BasePath.Sandbox);
        return new Fireblocks(opts);
    }
}