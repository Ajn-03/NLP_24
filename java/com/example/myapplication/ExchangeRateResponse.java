package com.example.myapplication;

import java.util.Map;

public class ExchangeRateResponse {
    private String base;
    private String disclaimer;
    private String license;
    private long timestamp;
    public Map<String, Double> rates;
    // Getters and setters
    public ExchangeRateResponse(String disclaimer, String license, long timestamp, Map<String, Double> rates) {
        this.disclaimer = disclaimer;
        this.license = license;
        this.timestamp = timestamp;
        this.rates = rates;
    }
    public Map<String, Double> getRates() {
        return rates;

    }
    public String getBase() {
        return base;
    }

    public String getLicense() {
        return license;

    } }
