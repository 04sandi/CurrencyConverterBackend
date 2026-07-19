package com.FirstProject.CurrencyConverter.controller;

import com.FirstProject.CurrencyConverter.dto.CurrencyDto;
import com.FirstProject.CurrencyConverter.dto.RequestDto;
import com.FirstProject.CurrencyConverter.dto.ResponseDto;
import com.FirstProject.CurrencyConverter.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;


    @PostMapping("/convert")
    public ResponseEntity<CurrencyDto> getConversion(@Valid @RequestBody RequestDto requestDto, @RequestHeader("api-key") String apiKey)
    {
        CurrencyDto currencyDto= currencyService.getConversion(
                requestDto.getFrom(),
                requestDto.getTo(),
                requestDto.getAmount(),
                apiKey
        );

        return ResponseEntity.ok(currencyDto);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CurrencyDto>> getAllConversion(@RequestHeader("api-key") String apiKey)
    {
        return ResponseEntity.ok(currencyService.getAllConversion(apiKey));
    }

    @GetMapping("/currencies")
    public ResponseEntity<ResponseDto> currencies()
    {
        return ResponseEntity.ok(currencyService.currencies());
    }

    @DeleteMapping("/history")
    public String deleteHistory(@RequestHeader("api-key") String apiKey)
    {
        currencyService.deleteHistory(apiKey);

        return "History deleted successfully";
    }
}
