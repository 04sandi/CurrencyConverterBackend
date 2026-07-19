package com.FirstProject.CurrencyConverter.controller;

import com.FirstProject.CurrencyConverter.dto.LoginDto;
import com.FirstProject.CurrencyConverter.dto.LoginResponseDto;
import com.FirstProject.CurrencyConverter.dto.RegisterDto;
import com.FirstProject.CurrencyConverter.dto.ResponseDto;
import com.FirstProject.CurrencyConverter.entity.User;
import com.FirstProject.CurrencyConverter.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto)
    {
        User user=new User();

        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());

        String apiKey= UUID.randomUUID().toString();

        user.setApiKey(apiKey);

        if(userRepository.findByEmail(registerDto.getEmail()).isPresent())
        {
            return ResponseEntity.badRequest().body("Email already exist");
        }
        userRepository.save(user);

        return ResponseEntity.ok("Registration Successful. Please Login.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto)
    {
        User user= userRepository.findByEmail(loginDto.getEmail()).orElse(null);

        if(user==null)
        {
            return ResponseEntity.badRequest().body(new LoginResponseDto("user not found",null));
        }

        if(!user.getPassword().equals(loginDto.getPassword()))
        {
            return ResponseEntity.badRequest().body(new LoginResponseDto("Invalid Password",null));
        }

        return ResponseEntity.ok(new LoginResponseDto("Login Successful",user.getApiKey()));
    }
}
