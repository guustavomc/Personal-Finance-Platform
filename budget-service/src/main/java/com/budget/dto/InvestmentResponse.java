package com.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestmentResponse {

    private long id;

    private String investmentType;

    private String assetSymbol;

    private BigDecimal amountInvested;

    private  BigDecimal quantity;

    private LocalDate investmentDate;

    private String currency;

    private BigDecimal alternateAmount;

    private String alternateCurrency;

    private String assetTag;

    public long getId() {
        return id;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getAmountInvested() {
        return amountInvested;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getInvestmentDate() {
        return investmentDate;
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

    public String getAssetTag() {
        return assetTag;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public void setAmountInvested(BigDecimal amountInvested) {
        this.amountInvested = amountInvested;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setInvestmentDate(LocalDate investmentDate) {
        this.investmentDate = investmentDate;
    }

    public void setCurrency(String currency) {
        this.currency=currency;

    }

    public void setAlternateAmount(BigDecimal alternateAmount) {
        this.alternateAmount = alternateAmount;
    }

    public void setAlternateCurrency(String alternateCurrency) {
        this.alternateCurrency = alternateCurrency;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }
}