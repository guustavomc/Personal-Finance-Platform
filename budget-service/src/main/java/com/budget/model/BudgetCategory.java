package com.budget.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget_categories")
public class BudgetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Budget budget;

    private String categoryName;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private BigDecimal plannedAmount;
    private BigDecimal percentageOfTotal;

    private BigDecimal actualSpent;

    private BigDecimal actualInvested;

    private LocalDateTime lastSynced;

    public Long getId() {
        return id;
    }

    public Budget getBudget() {
        return budget;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public BigDecimal getPercentageOfTotal() {
        return percentageOfTotal;
    }

    public BigDecimal getActualSpent() {
        return actualSpent;
    }

    public BigDecimal getActualInvested() {
        return actualInvested;
    }

    public LocalDateTime getLastSynced() {
        return lastSynced;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public void setPercentageOfTotal(BigDecimal percentageOfTotal) {
        this.percentageOfTotal = percentageOfTotal;
    }

    public void setActualSpent(BigDecimal actualSpent) {
        this.actualSpent = actualSpent;
    }

    public void setActualInvested(BigDecimal actualInvested) {
        this.actualInvested = actualInvested;
    }

    public void setLastSynced(LocalDateTime lastSynced) {
        this.lastSynced = lastSynced;
    }
}
