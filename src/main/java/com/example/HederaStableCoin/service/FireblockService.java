package com.example.HederaStableCoin.service;

import com.fireblocks.sdk.ApiResponse;
import com.fireblocks.sdk.Fireblocks;
import com.fireblocks.sdk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class FireblockService {

    private final Fireblocks fireblocks;

    @Autowired
    public FireblockService(Fireblocks fireblocks) {
        this.fireblocks = fireblocks;
    }

    public String createRawTransaction(String base64Tx, String assetId, String vaultAccountId) throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest()
                .operation(TransactionOperation.TRANSFER)
                .source(new SourceTransferPeerPath()
                        .type(TransferPeerPathType.VAULT_ACCOUNT)
                        .id(vaultAccountId))
                .destination(new DestinationTransferPeerPath()
                        .type(TransferPeerPathType.VAULT_ACCOUNT)
                        .id("1"))
                .amount(new TransactionRequestAmount("0.001"))
                .assetId("HBAR_TEST")
                .note("My first Java transaction!");

        String idempotencyKey = Integer.toString(new Random().nextInt());
        CompletableFuture<ApiResponse<CreateTransactionResponse>> responseFuture =
                fireblocks.transactions().createTransaction(transactionRequest, null, idempotencyKey);

        CreateTransactionResponse transactionResponse = responseFuture.get().getData();
        return transactionResponse.getId();
    }
}