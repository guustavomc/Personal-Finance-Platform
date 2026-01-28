package com.budget.model;

import java.time.LocalDate;

public enum BudgetPeriodType {
    WEEKLY(7),
    BI_WEEKLY(14),
    MONTHLY(30),
    QUARTERLY(90),
    SEMI_ANNUAL(180),
    ANNUAL(365);

    private int approximateDays;
    BudgetPeriodType(int approximateDays) {
        this.approximateDays=approximateDays;
    }

    public int getApproximateDays() {
        return approximateDays;
    }

    public LocalDate calculateEndDate(LocalDate startDate){
        return switch (this) {
            case WEEKLY -> startDate.plusWeeks(1).minusDays(1);
            case BI_WEEKLY -> startDate.plusWeeks(2).minusDays(1);
            case MONTHLY -> startDate.plusMonths(1).minusDays(1);
            case QUARTERLY -> startDate.plusMonths(3).minusDays(1);
            case SEMI_ANNUAL -> startDate.plusMonths(6).minusDays(1);
            case ANNUAL -> startDate.plusYears(1).minusDays(1);
        };
    }

}
