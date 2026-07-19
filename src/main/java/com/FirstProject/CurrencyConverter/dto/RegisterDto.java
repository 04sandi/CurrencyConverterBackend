package com.FirstProject.CurrencyConverter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank
    private String name;
    @Email(message = "Invalid Email format")
    private String email;
    @NotBlank
    @Size(min = 6,message = "password must be at least 6 characters")
    private String password;
}
