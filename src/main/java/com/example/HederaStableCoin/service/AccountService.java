package com.example.HederaStableCoin.service;

import com.example.HederaStableCoin.model.dto.AccountRequestDTO;
import com.example.HederaStableCoin.model.dto.AccountResponseDTO;
import com.example.HederaStableCoin.model.entity.AccountEntity;
import com.example.HederaStableCoin.repository.AccountRepository;
import com.example.HederaStableCoin.repository.InMemoryRoleStore;
import com.example.HederaStableCoin.repository.InMemoryStablecoinStore;
import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class AccountService {

    private final InMemoryRoleStore roleStore = new InMemoryRoleStore();
    @Autowired
    private Client hederaClient;
    @Autowired
    private InMemoryStablecoinStore inMemoryStablecoinStore;
    @Autowired
    private AccountRepository accountRepository;

    public AccountResponseDTO createAccount(AccountRequestDTO request) throws Exception {
        PrivateKey privateKey = PrivateKey.generateED25519();
        PublicKey publicKey = privateKey.getPublicKey();
        AccountCreateTransaction transaction = new AccountCreateTransaction().setKey(publicKey).setInitialBalance(new Hbar(10));
        TransactionResponse txResponse = transaction.execute(hederaClient);
        AccountId newAccountId = txResponse.getReceipt(hederaClient).accountId;
        roleStore.assignRole(newAccountId.toString(), request.getRole());
        roleStore.Storekeys(newAccountId.toString(), privateKey.toString(), publicKey.toString());
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountId(newAccountId.toString());
        response.setPrivateKey(privateKey.toString());
        response.setPublicKey(publicKey.toString());
        response.setRole(request.getRole());

        AccountEntity entity = new AccountEntity();
        entity.setAccountId(response.getAccountId());
        entity.setPrivateKey(response.getPrivateKey());
        entity.setPublicKey(response.getPublicKey());
        entity.setRole(response.getRole());
        accountRepository.save(entity);

        return response;
    }

    public Map<String, Object> getAccountBalance(String accountId) throws TimeoutException, PrecheckStatusException {
        AccountId id = AccountId.fromString(accountId);
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(id)
                .execute(hederaClient);

        Map<String, Object> response = new HashMap<>();
        response.put("hbarBalance", accountBalance.hbars.toTinybars());
        Map<String, Long> tokenBalances = new HashMap<>();

        accountBalance.tokens.forEach((tokenId, balanceAmount) ->
                tokenBalances.put(tokenId.toString(), balanceAmount.longValue()));
        response.put("tokenBalances", tokenBalances);
        return response;
    }

    public List<Map<String, String>> getAllAccounts() {
        List<AccountEntity> entities = accountRepository.findAll();
        List<Map<String, String>> accounts = new java.util.ArrayList<>();
        for (AccountEntity entity : entities) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", entity.getAccountId());
            map.put("privateKey", entity.getPrivateKey());
            map.put("publicKey", entity.getPublicKey());
            map.put("role", entity.getRole().toString());
            accounts.add(map);
        }
        return accounts;
    }

}
