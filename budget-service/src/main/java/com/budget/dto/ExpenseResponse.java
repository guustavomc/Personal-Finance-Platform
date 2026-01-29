package com.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;

    private String description;

    private String category;

    private BigDecimal valueSpent;

    private LocalDate date;

    private String paymentMethod;

    private BigDecimal totalPurchaseValue;

    private int numberOfInstallments = 1;
    private int currentInstallment = 1;

    private String currency;


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getValueSpent() {
        return valueSpent;
    }

    public LocalDate getDate() {
        return date;
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

    public String getCurrency() {
        return currency;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValueSpent(BigDecimal valueSpent) {
        this.valueSpent = valueSpent;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}