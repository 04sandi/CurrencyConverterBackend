package com.FirstProject.CurrencyConverter.service;


import com.FirstProject.CurrencyConverter.dto.CurrencyDto;
import com.FirstProject.CurrencyConverter.dto.ResponseDto;

import java.util.List;

public interface CurrencyService {

    CurrencyDto getConversion(String from, String to, double amount, String apiKey);

    List<CurrencyDto> getAllConversion(String apiKey);

    ResponseDto currencies();

    void deleteHistory(String apiKey);
}
