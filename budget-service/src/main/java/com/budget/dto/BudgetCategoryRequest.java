package com.budget.dto;

import com.budget.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class BudgetCategoryRequest {

    @NotBlank
    private String categoryName;

    @NotNull
    @PositiveOrZero
    private BigDecimal plannedAmount;

    @NotNull
    private CategoryType type;

    private BigDecimal percentageOfTotal;

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public CategoryType getType() {
        return type;
    }

    public BigDecimal getPercentageOfTotal() {
        return percentageOfTotal;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public void setPercentageOfTotal(BigDecimal percentageOfTotal) {
        this.percentageOfTotal = percentageOfTotal;
    }
}
