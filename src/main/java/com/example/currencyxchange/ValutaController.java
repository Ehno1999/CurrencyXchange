package com.example.currencyxchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ValutaController {

    private final ValutaService valutaService;

    @Autowired
    public ValutaController(ValutaService valutaService) {
        this.valutaService = valutaService;
    }

    @RequestMapping("/")
    public ModelAndView welcome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping(value = "find/{baseCurrency}/{targetCurrency}/{date}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findOrCreate(@PathVariable("baseCurrency") String baseCurrency,
                                                            @PathVariable("targetCurrency") String targetCurrency,
                                                            @PathVariable("date") String date) {

        Valuta existingValuta = valutaService.findOrCreateValuta(baseCurrency, targetCurrency, date);

        if (existingValuta == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Record not found after creation.");
            return ResponseEntity.status(404).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("date", existingValuta.getDate());
        response.put("baseCurrency", existingValuta.getBaseCurrency());
        response.put("targetCurrency", existingValuta.getTargetCurrency());
        response.put("value", existingValuta.getValue());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "exchange/{baseCurrency}/{targetCurrency}/{date}/{amount}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> find(@PathVariable("baseCurrency") String baseCurrency,
                                                    @PathVariable("targetCurrency") String targetCurrency,
                                                    @PathVariable("amount") double amount,
                                                    @PathVariable("date") String date) {

        Valuta existingValuta = valutaService.findOrCreateValuta(baseCurrency, targetCurrency, date);

        if (existingValuta == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Record not found after creation.");
            return ResponseEntity.status(404).body(response);
        }

        double result = valutaService.performCalculations(existingValuta.getValue(), amount);

        Map<String, Object> response = new HashMap<>();
        response.put("convertedAmount", result);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{baseCurrency}/{targetCurrency}/{date}", method = RequestMethod.GET)
    public ResponseEntity<String> saveExchangeRate(@PathVariable("baseCurrency") String baseCurrency,
                                                   @PathVariable("targetCurrency") String targetCurrency,
                                                   @PathVariable("date") String date) {
        try {
            String message = valutaService.saveExchangeRates(baseCurrency, targetCurrency, date);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
