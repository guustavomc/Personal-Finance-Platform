package com.example.investment.dto;

import com.example.investment.model.InvestmentType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WithdrawalResponse {


    private long id;

    private InvestmentType investmentType;

    private String assetSymbol;

    private BigDecimal proceeds;

    private  BigDecimal quantity;

    private LocalDate withdrawalDate;

    private BigDecimal fee;

    private BigDecimal alternateAmount;

    private String alternateCurrency;

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getProceeds() {
        return proceeds;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public BigDecimal getFee() {
        return fee;
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

    public void setProceeds(BigDecimal proceeds) {
        this.proceeds = proceeds;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setWithdrawalDate(LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public void setAlternateAmount(BigDecimal alternateAmount) {
        this.alternateAmount = alternateAmount;
    }

    public void setAlternateCurrency(String alternateCurrency) {
        this.alternateCurrency = alternateCurrency;
    }
}
