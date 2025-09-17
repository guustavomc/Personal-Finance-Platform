package com.example.investment.dto;

import com.example.investment.model.InvestmentType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateInvestmentRequest {

    @NotBlank(message = "Category is a mandatory field")

    private InvestmentType investmentType;

    private String assetSymbol;

    private BigDecimal amountInvested;

    private BigDecimal quantity;
    private LocalDate investmentDate;

    private String currency;

    private BigDecimal alternateAmount = BigDecimal.valueOf(0.0);

    private String alternateCurrency= "";

    public InvestmentType getInvestmentType() {
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

    public void setInvestmentType(InvestmentType investmentType) {
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
        this.currency = currency;
    }

    public void setAlternateAmount(BigDecimal alternateAmount) {
        this.alternateAmount = alternateAmount;
    }

    public void setAlternateCurrency(String alternateCurrency) {
        this.alternateCurrency = alternateCurrency;
    }
}


