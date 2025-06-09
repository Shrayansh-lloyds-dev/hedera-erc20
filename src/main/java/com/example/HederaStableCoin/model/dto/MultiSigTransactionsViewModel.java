package com.example.HederaStableCoin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MultiSigTransactionsViewModel {
    private List<MultiSigTransactionViewModel> transactions;
    private int page;
    private int limit;
    private long total;

}