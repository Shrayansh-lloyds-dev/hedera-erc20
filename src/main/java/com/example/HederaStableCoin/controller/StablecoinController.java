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
                    null, null, null, null
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
    public ResponseEntity<StableCoinViewModel> getInfo(@RequestBody GetStableCoinDetailsRequest request) {
        try {
            return ResponseEntity.ok(stablecoinService.getInfo(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/getBalanceOf")
    public ResponseEntity<Balance> getBalanceOf(@RequestBody GetAccountBalanceRequest request) {
        try {
            return ResponseEntity.ok(stablecoinService.getBalanceOf(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/getBalanceOfHBAR")
    public ResponseEntity<Balance> getBalanceOfHBAR(@RequestBody GetAccountBalanceHBARRequest request) {
        try {
            return ResponseEntity.ok(stablecoinService.getBalanceOfHBAR(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

