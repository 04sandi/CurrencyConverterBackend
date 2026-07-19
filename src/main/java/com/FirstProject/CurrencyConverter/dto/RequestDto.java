package com.FirstProject.CurrencyConverter.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {

    @NotNull(message = "From currency is required")
    private String from;
    @NotNull(message = "To currency is required")
    private String to;
    @DecimalMin(value = "0.01", message = "Minimum amount is 0.01")
    private Double amount;
}
