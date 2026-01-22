package com.budget.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Budget {

    private long id;

    private String userId;

    private String name;

    @Enumerated(EnumType.STRING)
    private BudgetPeriodType budgetPeriodType;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BudgetStatus budgetStatus;

    private BigDecimal totalPlannedAmount;
    private BigDecimal totalActualSpent;
    private BigDecimal totalActualInvested;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
