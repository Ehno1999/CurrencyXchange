package com.example.currencyxchange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ValutaRepository extends JpaRepository<Valuta, Integer> {

    // Custom query to find Valuta by currency, second currency, and associated date
    @Query("SELECT v FROM Valuta v WHERE UPPER(v.baseCurrency) = UPPER(:baseCurrency) AND UPPER(v.targetCurrency) = UPPER(:targetCurrency) AND v.date = :date")
    Valuta findByBaseCurrencyAndTargetCurrencyAndDateIgnoreCase(@Param("baseCurrency") String baseCurrency,
                                                                @Param("targetCurrency") String targetCurrency,
                                                                @Param("date") String date);

}