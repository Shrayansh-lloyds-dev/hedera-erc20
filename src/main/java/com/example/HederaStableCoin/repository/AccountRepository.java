package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    AccountEntity findByAccountId(String accountId);
}