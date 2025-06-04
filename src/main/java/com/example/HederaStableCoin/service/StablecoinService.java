package com.example.HederaStableCoin.service;


import com.example.HederaStableCoin.model.StablecoinDetails;
import com.example.HederaStableCoin.model.dto.*;
import com.example.HederaStableCoin.repository.InMemoryStablecoinStore;
import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class StablecoinService {

    @Autowired
    private Client hederaClient;

    @Autowired
    private InMemoryStablecoinStore stablecoinStore;

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
                .setKycKey(treasuryPublicKey)
                .freezeWith(hederaClient);

        TransactionResponse response = tx.sign(treasuryPrivateKey).execute(hederaClient);

        TransactionReceipt receipt = response.getReceipt(hederaClient);

        TokenId tokenId = receipt.tokenId;


        StablecoinDetails details = new StablecoinDetails(request.getName(), request.getSymbol(), request.getDecimals(), request.getInitialSupply(), AccountId.fromString(request.getTreasuryAccountId()), tokenId);
        stablecoinStore.saveStablecoin(tokenId, details);

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
        for (TokenId tokenId : stablecoinStore.getAllTokenIds()) {
            Map<String, Object> tokenData = new HashMap<>();
            TokenInfo tokenInfo = new TokenInfoQuery()
                    .setTokenId(tokenId)
                    .execute(hederaClient);

            AccountBalance accountBalance = new AccountBalanceQuery().setAccountId(tokenInfo.treasuryAccountId).execute(hederaClient);

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


    public StableCoinViewModel getInfo(GetStableCoinDetailsRequest request) throws Exception {
        TokenId tokenId = TokenId.fromString(request.getId());
        StablecoinDetails details = stablecoinStore.getStablecoin(tokenId);
        if (details == null) throw new Exception("Token not found");

        StableCoinViewModel vm = new StableCoinViewModel();
        vm.setTokenId(tokenId.toString());
        vm.setName(details.getName());
        vm.setSymbol(details.getSymbol());
        vm.setDecimals(details.getDecimals());
        vm.setInitialSupply(details.getInitialSupply());
        vm.setTreasuryAccountId(details.getTreasuryAccountId().toString());
        return vm;
    }

    public Balance getBalanceOf(GetAccountBalanceRequest request) throws Exception {
        AccountId accountId = AccountId.fromString(request.getTargetId());
        TokenId tokenId = TokenId.fromString(request.getTokenId());
        AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
        long raw = balance.tokens.getOrDefault(tokenId, 0L);

        // Get decimals for this token
        TokenInfo info = new TokenInfoQuery().setTokenId(tokenId).execute(hederaClient);
        BigDecimal value = new BigDecimal(raw).movePointLeft(info.decimals);
        return new Balance(value, info.decimals);
    }

    public Balance getBalanceOfHBAR(GetAccountBalanceHBARRequest request) throws Exception {
        AccountId accountId = AccountId.fromString(request.getTreasuryAccountId());
        AccountBalance balance = new AccountBalanceQuery().setAccountId(accountId).execute(hederaClient);
        BigDecimal value = new BigDecimal(balance.hbars.toTinybars()).movePointLeft(8); // HBAR has 8 decimals
        return new Balance(value, 8);
    }

}
