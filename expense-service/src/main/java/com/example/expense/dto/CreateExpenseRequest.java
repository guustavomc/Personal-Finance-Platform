package com.example.expense.dto;

import com.example.expense.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateExpenseRequest {

    private String description;

    @NotBlank(message = "Category is a mandatory field")
    private String category;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal totalPurchaseValue;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private PaymentMethod paymentMethod;

    private int numberOfInstallments = 1;

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalPurchaseValue() {
        return totalPurchaseValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTotalPurchaseValue(BigDecimal totalPurchaseValue) {
        this.totalPurchaseValue = totalPurchaseValue;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }


}
