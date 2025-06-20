package com.example.HederaStableCoin.controller;

import java.math.BigDecimal;

import com.example.HederaStableCoin.model.dto.EthTransactionDetailsDTO;
import com.example.HederaStableCoin.model.dto.EthTransactionResponseDTO;
import com.example.HederaStableCoin.model.dto.EthereumBalanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.HederaStableCoin.service.EthereumService;

@RestController
@RequestMapping("/api/ethereum")
public class EthereumController {

    @Autowired
    private EthereumService ethereumService;

    @GetMapping("/client-version")
    public String getClientVersion() throws Exception {
        return ethereumService.getClientVersion();
    }

    @GetMapping("/generate-address")
    public String generateAddress() throws Exception {
        return ethereumService.generateAddress();
    }

    @GetMapping("/transaction/{hash}")
    public EthTransactionDetailsDTO getTransactionByHash(@PathVariable String hash) throws Exception {
        return ethereumService.getTransactionByHash(hash);
    }

    @PostMapping("/send-eth")
    public EthTransactionResponseDTO sendEth(
            @RequestParam String fromPrivateKey,
            @RequestParam String toAddress,
            @RequestParam BigDecimal amountInEther
    ) throws Exception {
        return ethereumService.sendEthWithReceipt(fromPrivateKey, toAddress, amountInEther);
    }

    @GetMapping("/balance/{address}")
    public EthereumBalanceDTO getBalance(@PathVariable String address) throws Exception {
        return ethereumService.getBalance(address);
    }

    @GetMapping("/validate-address/{address}")
    public boolean isValidAddress(@PathVariable String address) {
        return ethereumService.isValidAddress(address);
    }
}