package com.example.HederaStableCoin.model.dto;

import com.example.HederaStableCoin.model.RoleType;

public class AccountRequestDTO {

    private String alias;
    private RoleType role;

    public AccountRequestDTO(String alias, RoleType role) {
        this.alias = alias;
        this.role = role;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
