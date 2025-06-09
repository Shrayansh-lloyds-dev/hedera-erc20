package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.entity.MultiSigTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiSigTransactionRepository extends JpaRepository<MultiSigTransactionEntity, String> {
    MultiSigTransactionEntity findByTransactionId(String transactionId);
}
