package com.example.HederaStableCoin.model.entity;

import com.example.HederaStableCoin.model.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    private String accountId;
    private String privateKey;
    private String publicKey;
    private RoleType role;

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }
}