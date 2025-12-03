package com.example.investment.dto;

import com.example.investment.model.InvestmentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioEvent {

    private InvestmentType investmentType;

    private String assetSymbol;

    private BigDecimal amount;

    private  BigDecimal quantity;

    private LocalDate date;

    private String currency;

    private String eventType;

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEventType() {
        return eventType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
