package com.example.HederaStableCoin.controller;


import com.example.HederaStableCoin.model.dto.PriceResponseDTO;
import com.example.HederaStableCoin.model.dto.PriceResponseWrapper;
import com.example.HederaStableCoin.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping
    public ResponseEntity<PriceResponseWrapper> getPrices(@RequestParam(defaultValue = "USD") String currency) {
        try {
            PriceResponseDTO priceResponseDTO = priceService.getPrices(currency);
            PriceResponseWrapper responseWrapper = new PriceResponseWrapper("success", "Prices fetched successfully", priceResponseDTO);
            return ResponseEntity.ok(responseWrapper);
        } catch (Exception e) {
            PriceResponseWrapper errorResponse = new PriceResponseWrapper("error", e.getMessage(), null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
