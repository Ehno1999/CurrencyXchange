package com.example.currencyxchange;

import javax.persistence.*;

@Entity
@Table(name = "Valuta")
public class Valuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valuta_id")
    private Integer id;

    @Column(name = "value")
    private double value;

    @Column(name = "base_currency")
    private String baseCurrency;

    @Column(name = "target_currency")
    private String targetCurrency;

    @Column(name = "date")
    private String date;

 //   public Integer getId() {
 //       return id;
 //   }
//
 //   public void setId(Integer id) {
 //       this.id = id;
 //   }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Valuta() {
    }

    public Valuta(String date, String targetCurrency, String baseCurrency, double value, Integer id) {
        this.date = date;
        this.targetCurrency = targetCurrency;
        this.baseCurrency = baseCurrency;
        this.value = value;
        this.id = id;
    }
}
