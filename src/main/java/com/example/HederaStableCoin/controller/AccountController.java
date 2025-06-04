package com.example.HederaStableCoin.controller;

import com.example.HederaStableCoin.model.dto.AccountRequestDTO;
import com.example.HederaStableCoin.model.dto.AccountResponseDTO;
import com.example.HederaStableCoin.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO request) {
        try {
            return accountService.createAccount(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating account: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAllAccounts());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("/{accountId}/stablecoin-balances")
    public Map<String, Object> getStablecoinBalances(@RequestParam String accountId) {
        try {
            return accountService.getAccountBalance(accountId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error fetching balances: " + e.getMessage());
        }
    }
}
