package com.example.currencyxchange;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ValutaRepositoryTest {


    private final ValutaRepository valutaRepository;

    @Autowired
    public ValutaRepositoryTest(ValutaRepository valutaRepository) {
        this.valutaRepository = valutaRepository;
    }

    @BeforeEach
    public void setUp() {
        Valuta valuta = new Valuta();
        valuta.setBaseCurrency("USD");
        valuta.setTargetCurrency("SEK");
        valuta.setDate("2025-01-01");
        valuta.setValue(10.5);
        valutaRepository.save(valuta);
    }

    @Test
    public void testFindByCurrencyAndSecondCurrencyAndDateIgnoreCase() {
        Valuta foundValuta = valutaRepository.findByBaseCurrencyAndTargetCurrencyAndDateIgnoreCase("USD", "SEK", "2025-01-01");

        assertNotNull(foundValuta);
        assertEquals("USD", foundValuta.getBaseCurrency());
        assertEquals("SEK", foundValuta.getTargetCurrency());
        assertEquals("2025-01-01", foundValuta.getDate());
        assertEquals(10.5, foundValuta.getValue());
    }
}