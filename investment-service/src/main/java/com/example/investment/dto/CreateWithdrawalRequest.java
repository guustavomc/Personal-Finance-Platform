package com.example.investment.dto;

import com.example.investment.model.InvestmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateWithdrawalRequest {

    @NotNull(message = "Category is a mandatory field")
    private InvestmentType investmentType;

    @NotBlank(message = "Asset symbol is required")
    private String assetSymbol;

    @NotNull(message = "Amount is required")
    private BigDecimal proceeds;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private  BigDecimal quantity;

    @NotNull(message = "Withdrawal date is required")
    private LocalDate withdrawalDate;

    private BigDecimal fee = BigDecimal.valueOf(0.0);

    private BigDecimal alternateAmount = BigDecimal.valueOf(0.0);

    private String alternateCurrency = "";

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
