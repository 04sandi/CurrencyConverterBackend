package com.FirstProject.CurrencyConverter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CurrencyDto {

    private Long id;
    private String fromCurrency;
    private String toCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;
    private LocalDateTime conversionTime;
}
