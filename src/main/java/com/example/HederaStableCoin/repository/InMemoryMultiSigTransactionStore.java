package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.dto.MultiSigTransaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryMultiSigTransactionStore {
    private final List<MultiSigTransaction> transactions = new ArrayList<>();

    public synchronized void add(MultiSigTransaction tx) {
        transactions.add(tx);
    }

    public synchronized void save(MultiSigTransaction tx) {
        System.out.println("Saving transaction: " + tx); // Add this line
        add(tx);
    }

    public synchronized List<MultiSigTransaction> getAll() {
        System.out.println("Retrieving all transactions, count: " + transactions.size()); // Add this line
        return new ArrayList<>(transactions);
    }
}