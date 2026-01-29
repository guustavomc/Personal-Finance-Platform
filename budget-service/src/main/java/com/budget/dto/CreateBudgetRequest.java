package com.budget.dto;

import com.budget.model.BudgetCategory;
import com.budget.model.BudgetPeriodType;
import com.budget.model.BudgetStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateBudgetRequest {

    @NotNull
    private String name;

    @NotNull
    private BudgetPeriodType budgetPeriodType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @Positive
    private BigDecimal totalPlannedAmount;

    @NotEmpty
    private List<BudgetCategory> categories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public BudgetPeriodType getBudgetPeriodType() {
        return budgetPeriodType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getTotalPlannedAmount() {
        return totalPlannedAmount;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudgetPeriodType(BudgetPeriodType budgetPeriodType) {
        this.budgetPeriodType = budgetPeriodType;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalPlannedAmount(BigDecimal totalPlannedAmount) {
        this.totalPlannedAmount = totalPlannedAmount;
    }

    public void setCategories(List<BudgetCategory> categories) {
        this.categories = categories;
    }
}
