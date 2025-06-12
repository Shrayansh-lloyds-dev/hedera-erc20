package com.example.HederaStableCoin.controller;


import com.example.HederaStableCoin.model.dto.*;
import com.example.HederaStableCoin.service.StablecoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stablecoin")
public class StablecoinController {

    @Autowired
    private StablecoinService stablecoinService;
    /**
     * Endpoint to create a new stablecoin.
     *
     * @param request The request containing the details for creating the stablecoin.
     * @return A response entity containing the result of the creation operation.
     */
    @PostMapping("/create")
    public ResponseEntity<StablecoinResponseDTO> createStablecoin(@RequestBody StablecoinRequestDTO request) {
        try {
            StablecoinResponseDTO response = stablecoinService.createStablecoin(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            StablecoinResponseDTO errorResponse = new StablecoinResponseDTO(
                    400,
                    "Error creating token: " + e.getMessage(),
                    null, null, null, null,null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getAllStablecoinBalances() {
        try {
            return ResponseEntity.ok(stablecoinService.getAllStablecoinBalances());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching stablecoin balances: " + e.getMessage());
        }
    }

    @PostMapping("/getInfo")
    public ResponseEntity<GetStableCoinInfoResponse> getInfo(@RequestBody GetStableCoinDetailsRequest request) {
        GetStableCoinInfoResponse response = stablecoinService.getInfo(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/getBalanceOf")
    public ResponseEntity<BalanceResponse> getBalanceOf(@RequestBody GetAccountBalanceRequest request) {
        BalanceResponse response = stablecoinService.getBalanceOf(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/getBalanceOfHBAR")
    public ResponseEntity<BalanceResponse> getBalanceOfHBAR(@RequestBody GetAccountBalanceHBARRequest request) {
        BalanceResponse response = stablecoinService.getBalanceOfHBAR(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/associate")
    public ResponseEntity<AssociateTokenResponse> associate(@RequestBody AssociateTokenRequest request) {
        AssociateTokenResponse response = stablecoinService.associateToken(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/isAccountAssociated")
    public ResponseEntity<IsAccountAssociatedResponse> isAccountAssociated(@RequestBody IsAccountAssociatedTokenRequest request) {
        IsAccountAssociatedResponse response = stablecoinService.isAccountAssociated(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/transferToken")
    public ResponseEntity<TransfersResponse> transferToken(@RequestBody TransfersRequest request) {
        TransfersResponse response = stablecoinService.transferToken(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/transferHbar")
    public ResponseEntity<TransferHbarResponse> transferHbar(@RequestBody TransferHbarRequest request) {
        TransferHbarResponse response = stablecoinService.transferHbar(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/getTransactions")
    public ResponseEntity<MultiSigTransactionsViewModel> getTransactions(@RequestBody GetTransactionsRequest request) {
        MultiSigTransactionsViewModel response = stablecoinService.getTransactions(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getTransactionStatus(@PathVariable String transactionId) {
        try {
            MultiSigTransactionViewModel tx = stablecoinService.getTransactionById(transactionId);
            if (tx == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
            }
            return ResponseEntity.ok(tx);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/mint")
    public ResponseEntity<MintTokenResponse> mintToken(@RequestBody MintTokenRequest request) {
        MintTokenResponse response = stablecoinService.mintToken(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

