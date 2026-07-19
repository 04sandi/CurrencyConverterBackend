package com.FirstProject.CurrencyConverter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResponseDto {

    private String base;
    private Map<String ,Double> rates;
}
