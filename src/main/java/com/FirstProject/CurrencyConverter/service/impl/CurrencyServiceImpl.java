package com.FirstProject.CurrencyConverter.service.impl;

import com.FirstProject.CurrencyConverter.dto.CurrencyDto;
import com.FirstProject.CurrencyConverter.dto.ResponseDto;
import com.FirstProject.CurrencyConverter.entity.Currency;
import com.FirstProject.CurrencyConverter.entity.User;
import com.FirstProject.CurrencyConverter.repository.CurrencyRepository;
import com.FirstProject.CurrencyConverter.repository.UserRepository;
import com.FirstProject.CurrencyConverter.service.CurrencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    @Override
    public CurrencyDto getConversion(String from, String to, double amount, String apiKey) {

        User user=userRepository.findByApiKey(apiKey).orElseThrow(() -> new IllegalArgumentException("Invalid API key"));

        Long userId=user.getId();

        RestTemplate obj=new RestTemplate();

        String url="https://api.exchangerate-api.com/v4/latest/" + from;

        ResponseDto responseDto=obj.getForObject(url,ResponseDto.class);

        if (responseDto == null || responseDto.getRates() == null) {
            throw new RuntimeException("Failed to fetch exchange rates from API");
        }

        double rate=responseDto.getRates().get(to);

        double convertedAmount=amount*rate;

        Currency currency=new Currency();

        currency.setFromCurrency(from);
        currency.setToCurrency(to);
        currency.setRate(rate);
        currency.setAmount(amount);
        currency.setConvertedAmount(convertedAmount);
        currency.setConversionTime(LocalDateTime.now());
        currency.setUserId(userId);

        Currency currency1=currencyRepository.save(currency);

        return modelMapper.map(currency1,CurrencyDto.class);
    }

    @Override
    public List<CurrencyDto> getAllConversion(String apiKey) {

        User user=userRepository.findByApiKey(apiKey).orElseThrow(() -> new IllegalArgumentException("Invalid API key"));

        Long userId=user.getId();

        List<Currency> currencies=currencyRepository.findByUserId(userId);

        return currencies.stream().map(currency -> modelMapper.map(currency ,CurrencyDto.class)).toList();
    }

    @Override
    public ResponseDto currencies() {

        RestTemplate obj=new RestTemplate();

        String currency="USD";

        String url="https://api.exchangerate-api.com/v4/latest/"+currency;

        return obj.getForObject(url,ResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteHistory(String apiKey) {

        User user= userRepository.findByApiKey(apiKey).orElseThrow(() -> new IllegalArgumentException("Invalid API Key"));

        currencyRepository.deleteByUserId(user.getId());
    }
}
