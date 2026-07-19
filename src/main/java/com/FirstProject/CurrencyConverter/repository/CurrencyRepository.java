package com.FirstProject.CurrencyConverter.repository;

import com.FirstProject.CurrencyConverter.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency , Long> {

    List<Currency> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
