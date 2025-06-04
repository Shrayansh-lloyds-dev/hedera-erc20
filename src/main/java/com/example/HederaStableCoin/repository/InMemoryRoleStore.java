package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class InMemoryRoleStore {
    private final Map<String, RoleType> roleMap  = new ConcurrentHashMap<>();
    private final Map<String, String> privateKeyStore = new HashMap<>();
    private final Map<String, String> publicKeyStore = new HashMap<>();

    public void assignRole(String accountId, RoleType role) {
        roleMap.put(accountId, role);
    }
    public RoleType getRole(String accountId) {
        return roleMap.get(accountId);
    }

    public void Storekeys(String accountId, String privateKey, String publicKey) {
       privateKeyStore.put(accountId, privateKey);
       publicKeyStore.put(accountId, publicKey);
    }

    public List <Map<String, String>> getAllAccounts() {
        List<Map<String, String>> result = new ArrayList<>();
        for(String accountId : roleMap.keySet()) {
            Map<String, String> accountInfo = new HashMap<>();
            accountInfo.put("accountId", accountId);
            accountInfo.put("role", roleMap.get(accountId).toString());
            accountInfo.put("privateKey", privateKeyStore.getOrDefault(accountId, "N/A"));
            accountInfo.put("publicKey", publicKeyStore.getOrDefault(accountId, "N/A"));
            result.add(accountInfo);
        }

      return result;
    }
}