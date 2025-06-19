package com.example.HederaStableCoin.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class FireblocksJwtGenerator {

    public static String generateJwt(String apiKey, String secretPath) throws Exception {
        String privateKeyPem = new String(Files.readAllBytes(Paths.get(secretPath)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);

        long now = System.currentTimeMillis();
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privateKey);

        return JWT.create()
                .withIssuer(apiKey)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + 300000))
                .sign(algorithm);
    }
}