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

    private BigDecimal alternateAmount;
    private String alternateCurrency;

    private BigDecimal fee;

    private String eventType;

    private String assetTag;


    public PortfolioEvent(InvestmentType investmentType,String assetSymbol,BigDecimal amount, BigDecimal quantity,LocalDate date, String currency, BigDecimal fee,String eventType, String assetTag){
        this.investmentType=investmentType;
        this.assetSymbol=assetSymbol;
        this.amount = amount;
        this.quantity = quantity;
        this.date = date;
        this.currency = currency;
        this.fee=fee;
        this.eventType=eventType;
        this.assetTag=assetTag;
    }

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

    public BigDecimal getAlternateAmount() {
        return alternateAmount;
    }

    public String getAlternateCurrency() {
        return alternateCurrency;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public String getEventType() {
        return eventType;
    }

    public String getAssetTag() {
        return assetTag;
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

    public void setAlternateAmount(BigDecimal alternateAmount) {
        this.alternateAmount = alternateAmount;
    }

    public void setAlternateCurrency(String alternateCurrency) {
        this.alternateCurrency = alternateCurrency;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }
}
