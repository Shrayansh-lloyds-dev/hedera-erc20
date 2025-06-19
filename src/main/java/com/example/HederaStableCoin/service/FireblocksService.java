package com.example.HederaStableCoin.service;

import com.fireblocks.sdk.Fireblocks;
import com.fireblocks.sdk.model.*;
import com.fireblocks.sdk.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class FireblocksService {

    @Autowired
    private Fireblocks fireblocks;

    public String signWithFireblocks(byte[] unsignedTxBytes, String vaultAccountId, String assetId) throws Exception {
        String base64Tx = Base64.getEncoder().encodeToString(unsignedTxBytes);

        TransactionRequest req = new TransactionRequest()
                .operation(TransactionOperation.RAW)
                .assetId(assetId)
                .source(new SourceTransferPeerPath()
                        .type(TransferPeerPathType.VAULT_ACCOUNT)
                        .id(vaultAccountId))
                .extraParameters(Map.of(
                        "rawMessageData", Map.of(
                                "messages", List.of(
                                        Map.of("content", base64Tx)
                                )
                        )
                ));

        ApiResponse<CreateTransactionResponse> apiResponse =
                fireblocks.transactions().createTransaction(req, null, null).get();

        return apiResponse.getData().getId();
    }
}