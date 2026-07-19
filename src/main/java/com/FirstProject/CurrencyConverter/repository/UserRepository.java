package com.FirstProject.CurrencyConverter.repository;

import com.FirstProject.CurrencyConverter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByApiKey(String apiKey);
}
