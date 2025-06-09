package com.example.HederaStableCoin.service;


import com.example.HederaStableCoin.model.StablecoinDetails;
import com.example.HederaStableCoin.model.dto.*;
import com.example.HederaStableCoin.model.entity.MultiSigTransactionEntity;
import com.example.HederaStableCoin.model.entity.StablecoinEntity;
import com.example.HederaStableCoin.repository.InMemoryStablecoinStore;
import com.example.HederaStableCoin.repository.MultiSigTransactionRepository;
import com.example.HederaStableCoin.repository.StablecoinRepository;
import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class StablecoinService {

    @Autowired
    private Client hederaClient;

    @Autowired
    private InMemoryStablecoinStore stablecoinStore;

    @Autowired
    private MultiSigTransactionRepository multiSigTransactionRepository ;

    @Autowired
    private StablecoinRepository stablecoinRepository;

    public StablecoinResponseDTO createStablecoin(StablecoinRequestDTO request) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        AccountId treasuryAccountId = AccountId.fromString(request.getTreasuryAccountId());
        PrivateKey treasuryPrivateKey = PrivateKey.fromString(request.getTreasuryPrivateKey());
        PublicKey treasuryPublicKey = treasuryPrivateKey.getPublicKey();

        TokenCreateTransaction tx = new TokenCreateTransaction()
                .setTokenName(request.getName())
                .setTokenSymbol(request.getSymbol())
                .setDecimals(request.getDecimals())
                .setInitialSupply(request.getInitialSupply())
                .setTreasuryAccountId(treasuryAccountId)
                .setAdminKey(treasuryPublicKey)
                .setSupplyKey(treasuryPublicKey)
                .setFreezeKey(treasuryPublicKey)
                .setWipeKey(treasuryPublicKey)
                .freezeWith(hederaClient);

        TransactionResponse response = tx.sign(treasuryPrivateKey).execute(hederaClient);
        TransactionReceipt receipt = response.getReceipt(hederaClient);
        TokenId tokenId = receipt.tokenId;

        StablecoinEntity entity = new StablecoinEntity();
        entity.setTokenId(tokenId.toString());
        entity.setName(request.getName());
        entity.setSymbol(request.getSymbol());
        entity.setDecimals(request.getDecimals());
        entity.setInitialSupply(request.getInitialSupply());
        entity.setTreasuryAccountId(request.getTreasuryAccountId());
        stablecoinRepository.save(entity);

        return new StablecoinResponseDTO(
                200,
                "Token created successfully",
                tokenId.toString(),
                request.getName(),
                request.getSymbol(),
                receipt
        );
    }

    public List<Map<String, Object>> getAllStablecoinBalances() throws TimeoutException, PrecheckStatusException {
        List<Map<String, Object>> result = new ArrayList<>();
        List<StablecoinEntity> stablecoins = stablecoinRepository.findAll();
        for (StablecoinEntity entity : stablecoins) {
            Map<String, Object> tokenData = new HashMap<>();
            TokenId tokenId = TokenId.fromString(entity.getTokenId());
            TokenInfo tokenInfo = new TokenInfoQuery()
                    .setTokenId(tokenId)
                    .execute(hederaClient);

            AccountBalance accountBalance = new AccountBalanceQuery()
                    .setAccountId(tokenInfo.treasuryAccountId)
                    .execute(hederaClient);

            tokenData.put("tokenId", tokenId.toString());
            tokenData.put("name", tokenInfo.name);
            tokenData.put("symbol", tokenInfo.symbol);
            tokenData.put("decimals", tokenInfo.decimals);
            tokenData.put("initialSupply", tokenInfo.totalSupply);
            tokenData.put("treasuryAccountId", tokenInfo.treasuryAccountId.toString());
            tokenData.put("hbarBalance", accountBalance.hbars.toTinybars());
            tokenData.put("tokenBalance", accountBalance.tokens.getOrDefault(tokenId, 0L));
            result.add(tokenData);
        }
        return result;
    }

    public GetStableCoinInfoResponse getInfo(GetStableCoinDetailsRequest request) {
        try {
            TokenId tokenId = TokenId.fromString(request.getId());
            StablecoinDetails details = stablecoinStore.getStablecoin(tokenId);
            if (details == null) {
                return new GetStableCoinInfoResponse(404, "Token not found", null);
            }
            StableCoinViewModel vm = new StableCoinViewModel();
            vm.setTokenId(tokenId.toString());
            vm.setName(details.getName());
            vm.setSymbol(details.getSymbol());
            vm.setDecimals(details.getDecimals());
            vm.setInitialSupply(details.getInitialSupply());
            vm.setTreasuryAccountId(details.getTreasuryAccountId().toString());
            return new GetStableCoinInfoResponse(200, "Token info fetched successfully", vm);
        } catch (Exception e) {
            return new GetStableCoinInfoResponse(500, "Error: " + e.getMessage(), null);
        }
    }

    public BalanceResponse getBalanceOf(GetAccountBalanceRequest request) {
        try {
            AccountId accountId = AccountId.fromString(request.getTargetId());
            TokenId tokenId = TokenId.fromString(request.getTokenId());
            AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
            long raw = balance.tokens.getOrDefault(tokenId, 0L);
            TokenInfo info = new TokenInfoQuery().setTokenId(tokenId).execute(hederaClient);
            BigDecimal value = new BigDecimal(raw).movePointLeft(info.decimals);
            return new BalanceResponse(200, "Balance fetched successfully", tokenId.toString(), accountId.toString(), value, info.decimals);
        } catch (Exception e) {
            return new BalanceResponse(500, "Error: " + e.getMessage(), request.getTokenId(), request.getTargetId(), null, 0);
        }
    }

    public BalanceResponse getBalanceOfHBAR(GetAccountBalanceHBARRequest request) {
        try {
            AccountId accountId = AccountId.fromString(request.getTreasuryAccountId());
            AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
            BigDecimal value = new BigDecimal(balance.hbars.toTinybars()).movePointLeft(8);
            return new BalanceResponse(200, "HBAR balance fetched successfully", null, accountId.toString(), value, 8);
        } catch (Exception e) {
            return new BalanceResponse(500, "Error: " + e.getMessage(), null, request.getTreasuryAccountId(), null, 0);
        }
    }

    public AssociateTokenResponse associateToken(AssociateTokenRequest request) {
        try {
            AccountId accountId = AccountId.fromString(request.getAccountId());
            PrivateKey privateKey = PrivateKey.fromString(request.getPrivateKey());
            TokenId tokenId = TokenId.fromString(request.getTokenId());

            TokenAssociateTransaction associateTx = new TokenAssociateTransaction()
                    .setAccountId(accountId)
                    .setTokenIds(List.of(tokenId))
                    .freezeWith(hederaClient)
                    .sign(privateKey);

            TransactionResponse response = associateTx.execute(hederaClient);
            TransactionReceipt receipt = response.getReceipt(hederaClient);

            return new AssociateTokenResponse(
                    200,
                    "Association successful",
                    accountId.toString(),
                    tokenId.toString(),
                    receipt
            );
        } catch (Exception e) {
            return new AssociateTokenResponse(
                    500,
                    "Association failed: " + e.getMessage(),
                    request.getAccountId(),
                    request.getTokenId(),
                    null
            );
        }
    }

    public IsAccountAssociatedResponse isAccountAssociated(IsAccountAssociatedTokenRequest request) {
        try {
            AccountId accountId = AccountId.fromString(request.getTargetId());
            TokenId tokenId = TokenId.fromString(request.getTokenId());
            AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
            boolean associated = balance.tokens.containsKey(tokenId);

            return new IsAccountAssociatedResponse(
                    200,
                    "Association status fetched",
                    tokenId.toString(),
                    accountId.toString(),
                    associated
            );
        } catch (Exception e) {
            return new IsAccountAssociatedResponse(
                    500,
                    "Error: " + e.getMessage(),
                    request.getTokenId(),
                    request.getTargetId(),
                    false
            );
        }
    }


    public TransfersResponse transferToken(TransfersRequest request) {
        try {
            AccountId fromAccountId = AccountId.fromString(request.getFromAccountId());
            PrivateKey fromPrivateKey = PrivateKey.fromString(request.getFromPrivateKey());
            TokenId tokenId = TokenId.fromString(request.getTokenId());
            AccountId toAccountId = AccountId.fromString(request.getTargetId());
            long amount = request.getAmount();

            TransferTransaction tx = new TransferTransaction()
                    .addTokenTransfer(tokenId, fromAccountId, -amount)
                    .addTokenTransfer(tokenId, toAccountId, amount)
                    .freezeWith(hederaClient)
                    .sign(fromPrivateKey);

            TransactionResponse response = tx.execute(hederaClient);
            TransactionReceipt receipt = response.getReceipt(hederaClient);

            MultiSigTransactionEntity txEntity = new MultiSigTransactionEntity();
            txEntity.setTransactionId(response.transactionId.toString());
            txEntity.setFromAccount(fromAccountId.toString());
            txEntity.setToAccount(toAccountId.toString());
            txEntity.setAmount(amount);
            txEntity.setStatus("COMPLETED");
            txEntity.setCreatedAt(java.time.Instant.now());
            txEntity.setTokenId(tokenId.toString());
            txEntity.setIsHbar(false);
            multiSigTransactionRepository.save(txEntity);
            return new TransfersResponse(
                    200,
                    "Token transfer successful",
                    tokenId.toString(),
                    fromAccountId.toString(),
                    receipt
            );
        } catch (Exception e) {
            return new TransfersResponse(
                    500,
                    "Token transfer failed: " + e.getMessage(),
                    request.getTokenId(),
                    request.getFromAccountId(),
                    null
            );
        }
    }

    public TransferHbarResponse transferHbar(TransferHbarRequest request) {
        try {
            AccountId fromAccountId = AccountId.fromString(request.getFromAccountId());
            PrivateKey fromPrivateKey = PrivateKey.fromString(request.getFromPrivateKey());
            AccountId toAccountId = AccountId.fromString(request.getToAccountId());
            long amount = request.getAmount();

            TransferTransaction tx = new TransferTransaction()
                    .addHbarTransfer(fromAccountId, Hbar.fromTinybars(-amount))
                    .addHbarTransfer(toAccountId, Hbar.fromTinybars(amount))
                    .freezeWith(hederaClient)
                    .sign(fromPrivateKey);

            TransactionResponse response = tx.execute(hederaClient);
            TransactionReceipt receipt = response.getReceipt(hederaClient);

            MultiSigTransactionEntity txEntity = new MultiSigTransactionEntity();
            txEntity.setTransactionId(response.transactionId.toString());
            txEntity.setFromAccount(fromAccountId.toString());
            txEntity.setToAccount(toAccountId.toString());
            txEntity.setAmount(amount);
            txEntity.setStatus("COMPLETED");
            txEntity.setCreatedAt(java.time.Instant.now());
            txEntity.setTokenId(null);
            txEntity.setIsHbar(true);
            multiSigTransactionRepository.save(txEntity);
            return new TransferHbarResponse(
                    200,
                    "HBAR transfer successful",
                    fromAccountId.toString(),
                    toAccountId.toString(),
                    response.transactionId.toString(),
                    receipt
            );
        } catch (Exception e) {
            return new TransferHbarResponse(
                    500,
                    "HBAR transfer failed: " + e.getMessage(),
                    request.getFromAccountId(),
                    request.getToAccountId(),
                    null,
                    null
            );
        }
    }

    public MultiSigTransactionsViewModel getTransactions(GetTransactionsRequest request) {
        List<MultiSigTransactionEntity> allEntities = multiSigTransactionRepository.findAll();

        List<MultiSigTransactionEntity> filtered = allEntities;
        String account = request.getAccount();
        if (account != null && !account.isEmpty()) {
            filtered = allEntities.stream()
                    .filter(tx -> tx.getFromAccount().equals(account) || tx.getToAccount().equals(account))
                    .collect(Collectors.toList());
        }

        List<MultiSigTransactionViewModel> pageList;
        if (filtered.isEmpty() && account != null && !account.isEmpty()) {
            // Fetch HBAR balance
            BigDecimal hbarBalance = null;
            try {
                AccountId accountId = AccountId.fromString(account);
                AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
                hbarBalance = new BigDecimal(balance.hbars.toTinybars()).movePointLeft(8);
            } catch (Exception e) {
                hbarBalance = BigDecimal.ZERO;
            }

            MultiSigTransactionViewModel noTransfer = new MultiSigTransactionViewModel();
            noTransfer.setTransactionId("N/A");
            noTransfer.setFromAccount(account);
            noTransfer.setToAccount(account);
            noTransfer.setAmount(0);
            noTransfer.setStatus("No Transfer");
            noTransfer.setCreatedAt(null);
            noTransfer.setHbar(false);
            noTransfer.setTokenId(null);
            // Optionally, add fields for hbarBalance/tokenBalance if you want to show them

            pageList = List.of(noTransfer);
        } else {
            int page = request.getPage() > 0 ? request.getPage() : 1;
            int limit = request.getLimit() > 0 ? request.getLimit() : filtered.size();
            int fromIndex = (page - 1) * limit;
            int toIndex = Math.min(fromIndex + limit, filtered.size());

            pageList = filtered.subList(
                    Math.min(fromIndex, filtered.size()),
                    Math.min(toIndex, filtered.size())
            ).stream().map(tx -> {
                MultiSigTransactionViewModel vm = new MultiSigTransactionViewModel();
                vm.setTransactionId(tx.getTransactionId());
                vm.setFromAccount(tx.getFromAccount());
                vm.setToAccount(tx.getToAccount());
                if (tx.isHbar()) {
                    vm.setAmount(tx.getAmount() / 100_000_000.0);
                } else {
                    vm.setAmount(tx.getAmount());
                }
                vm.setStatus(tx.getStatus());
                vm.setCreatedAt(tx.getCreatedAt());
                vm.setHbar(tx.isHbar());
                vm.setTokenId(tx.getTokenId());
                return vm;
            }).collect(Collectors.toList());
        }

        MultiSigTransactionsViewModel result = new MultiSigTransactionsViewModel();
        result.setTransactions(pageList);
        result.setPage(request.getPage());
        result.setLimit(request.getLimit());
        result.setTotal(filtered.size());
        return result;
    }
}
