package com.budget.dto;

import com.budget.model.BudgetCategory;
import com.budget.model.BudgetPeriodType;
import com.budget.model.BudgetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BudgetResponse {

    private long id;

    private String userId;

    private String name;

    private BudgetPeriodType budgetPeriodType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BudgetStatus budgetStatus;

    private BigDecimal totalPlannedAmount;
    private BigDecimal totalActualSpent;
    private BigDecimal totalActualInvested;

    private List<BudgetCategory> categories = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

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

    public BudgetStatus getBudgetStatus() {
        return budgetStatus;
    }

    public BigDecimal getTotalPlannedAmount() {
        return totalPlannedAmount;
    }

    public BigDecimal getTotalActualSpent() {
        return totalActualSpent;
    }

    public BigDecimal getTotalActualInvested() {
        return totalActualInvested;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setBudgetStatus(BudgetStatus budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public void setTotalPlannedAmount(BigDecimal totalPlannedAmount) {
        this.totalPlannedAmount = totalPlannedAmount;
    }

    public void setTotalActualSpent(BigDecimal totalActualSpent) {
        this.totalActualSpent = totalActualSpent;
    }

    public void setTotalActualInvested(BigDecimal totalActualInvested) {
        this.totalActualInvested = totalActualInvested;
    }

    public void setCategories(List<BudgetCategory> categories) {
        this.categories = categories;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
