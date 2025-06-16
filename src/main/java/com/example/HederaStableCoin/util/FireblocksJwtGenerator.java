package com.example.HederaStableCoin.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class FireblocksJwtGenerator {

    private static final String API_KEY = "514c25b1-8e87-4d4b-8669-65530d139f5c";
    private static final String PRIVATE_KEY_PATH = "src/main/resources/fireblocks_secret.key";

    public String generateJwt() {
        try {
            // Read PEM file
            String privateKeyPem = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH)));

            // Clean PEM headers and decode
            privateKeyPem = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);

            // Build JWT with 5 min expiry
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date exp = new Date(nowMillis + 5 * 60 * 1000);

            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String jwt = JWT.create()
                    .withIssuer(API_KEY)
                    .withIssuedAt(now)
                    .withExpiresAt(exp)
                    .sign(algorithm);
           System.out.println("Generated JWT: " + jwt); // For debugging purposes
            return jwt;
        } catch (Exception e) {
            throw new RuntimeException("JWT generation failed: " + e.getMessage(), e);
        }
    }
}