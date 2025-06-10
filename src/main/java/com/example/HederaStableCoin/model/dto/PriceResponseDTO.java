package com.example.HederaStableCoin.model.dto;

public class PriceResponseDTO {

    private double BTC;
    private double ETH;
    private double HBAR;

    public PriceResponseDTO(double BTC, double ETH, double HBAR) {
        this.BTC = BTC;
        this.ETH = ETH;
        this.HBAR = HBAR;
    }

    public double getBTC() {
        return BTC;
    }

    public void setBTC(double BTC) {
        this.BTC = BTC;
    }

    public double getETH() {
        return ETH;
    }

    public void setETH(double ETH) {
        this.ETH = ETH;
    }

    public double getHBAR() {
        return HBAR;
    }

    public void setHBAR(double HBAR) {
        this.HBAR = HBAR;
    }
}
