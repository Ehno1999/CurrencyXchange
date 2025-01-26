package com.example.currencyxchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ValutaService {

    private final ValutaRepository valutaRepository;

    @Autowired
    public ValutaService(ValutaRepository valutaRepository) {
        this.valutaRepository = valutaRepository;
    }
    public double performCalculations(double value, double amount) {
        System.out.println("Performing calculations...");
        return value * amount;
    }
    // Find or create a Valuta record
    public Valuta findOrCreateValuta(String baseCurrency, String targetCurrency, String date) {
        Valuta existingValuta = valutaRepository.findByBaseCurrencyAndTargetCurrencyAndDateIgnoreCase(baseCurrency, targetCurrency, date);

        if (existingValuta == null) {
            System.out.println("Record not found: " + baseCurrency + ", " + targetCurrency + ", " + date);

            String creationUrl = createValutaCreationUrl(baseCurrency, targetCurrency, date);

            boolean creationSuccess = createValutaRecord(creationUrl);

            if (creationSuccess) {
                existingValuta = valutaRepository.findByBaseCurrencyAndTargetCurrencyAndDateIgnoreCase(baseCurrency, targetCurrency, date);
            }
        }

        return existingValuta;
    }

    // Save exchange rates from external API
    public String saveExchangeRates(String baseCurrency, String targetCurrency, String date) {
        try {
            String url = String.format("https://api.riksbank.se/swea/v1/CrossRates/%s/%s/%s", baseCurrency, targetCurrency, date);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            List<Map<String, Object>> exchangeRates = new ObjectMapper().readValue(response.getBody(),
                    new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Map.class));

            for (Map<String, Object> exchangeRate : exchangeRates) {
                String value = String.valueOf(exchangeRate.get("value"));
                String exchangeDate = String.valueOf(exchangeRate.get("date"));

                Valuta valuta = new Valuta();
                valuta.setValue(Double.parseDouble(value));
                valuta.setBaseCurrency(getRealNames(baseCurrency));
                valuta.setTargetCurrency(getRealNames(targetCurrency));
                valuta.setDate(exchangeDate);

                if (valutaRepository.findByBaseCurrencyAndTargetCurrencyAndDateIgnoreCase(
                        valuta.getBaseCurrency(), valuta.getTargetCurrency(), exchangeDate) == null) {
                    valutaRepository.save(valuta);
                }
            }

            return "Unique data saved to database successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        }
    }

    // Construct URL for creating a Valuta record
    private String createValutaCreationUrl(String baseCurrency, String targetCurrency, String date) {
        Valuta valuta = new Valuta();
        return sendover(baseCurrency, targetCurrency, date);
    }

    // Send request to create a Valuta record
    private boolean createValutaRecord(String creationUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(creationUrl, String.class);
            System.out.println("Request successfully sent to: " + creationUrl);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send request to: " + creationUrl);
            e.printStackTrace();
            return false;
        }
    }

    public String sendover(String baseCurrency, String targetCurrency, String date) {
        return "http://localhost:8080/" + getRealNames(baseCurrency) + "/" + getRealNames(targetCurrency) + "/" + date;
    }

    public String getRealNames(String currency) {
        switch (currency.toUpperCase()) {
            case "SEKETT":
                return "SEK";
            case "SEK":
                return "SEKETT";
            case "SEKEURPMI":
                return "EURO";
            case "EURO":
                return "SEKEURPMI";
            case "USD":
                return "SEKUSDPMI";
            case "SEKUSDPMI":
                return "USD";
            default:
                return "Currency";
        }
    }
}
