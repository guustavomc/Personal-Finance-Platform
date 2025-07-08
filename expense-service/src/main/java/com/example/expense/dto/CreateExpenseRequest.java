package com.example.expense.dto;

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
    private BigDecimal valueSpent;

    @NotNull(message = "Date is required")
    private LocalDate date;

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
}
