package com.budget.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private CategoryType type;

    private String description;

    private String budgetItemCategory;

    private BigDecimal amount;

    private LocalDate date;

    private String assetSymbol;

    private String assetTag;

    private  BigDecimal quantity;

    private String currency;

    private BigDecimal alternateAmount;
    private String alternateCurrency;

    private String paymentMethod;
    private BigDecimal totalPurchaseValue;
    private int numberOfInstallments = 1;
    private int currentInstallment = 1;

    public long getId() {
        return id;
    }

    public CategoryType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getBudgetItemCategory() {
        return budgetItemCategory;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public BigDecimal getQuantity() {
        return quantity;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getTotalPurchaseValue() {
        return totalPurchaseValue;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public int getCurrentInstallment() {
        return currentInstallment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBudgetItemCategory(String budgetItemCategory) {
        this.budgetItemCategory = budgetItemCategory;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTotalPurchaseValue(BigDecimal totalPurchaseValue) {
        this.totalPurchaseValue = totalPurchaseValue;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public void setCurrentInstallment(int currentInstallment) {
        this.currentInstallment = currentInstallment;
    }
}
