package com.example.investment.model;

import java.math.BigDecimal;

public class AssetHolding {

    private InvestmentType investmentType;
    private String assetSymbol;
    private BigDecimal totalAmountInvested;
    private BigDecimal totalQuantity;
    private String primaryCurrency;
    private BigDecimal alternateTotalAmountInvested;
    private String alternateCurrency;
    
    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getTotalAmountInvested() {
        return totalAmountInvested;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public String getPrimaryCurrency() {
        return primaryCurrency;
    }

    public BigDecimal getAlternateTotalAmountInvested() {
        return alternateTotalAmountInvested;
    }

    public String getAlternateCurrency() {
        return alternateCurrency;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public void setTotalAmountInvested(BigDecimal totalAmountInvested) {
        this.totalAmountInvested = totalAmountInvested;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setPrimaryCurrency(String primaryCurrency) {
        this.primaryCurrency = primaryCurrency;
    }

    public void setAlternateTotalAmountInvested(BigDecimal alternateTotalAmountInvested) {
        this.alternateTotalAmountInvested = alternateTotalAmountInvested;
    }

    public void setAlternateCurrency(String alternateCurrency) {
        this.alternateCurrency = alternateCurrency;
    }
}
