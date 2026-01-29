package com.budget.service;

import com.budget.dto.BudgetResponse;
import com.budget.dto.CreateBudgetRequest;
import com.budget.model.Budget;
import com.budget.model.BudgetStatus;
import com.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseServiceClient expenseServiceClient;

    @Autowired
    private InvestmentServiceClient investmentServiceClient;

    public BudgetResponse createBudget(CreateBudgetRequest createBudgetRequest){
        Budget budget = new Budget();
        budget.setName(createBudgetRequest.getName());
        budget.setBudgetPeriodType(createBudgetRequest.getBudgetPeriodType());
        budget.setStartDate(createBudgetRequest.getStartDate());

        budget.setEndDate(adjustBudgetEndDate(createBudgetRequest));

        budget.setBudgetStatus(BudgetStatus.ACTIVE);

        budget.setTotalPlannedAmount(createBudgetRequest.getTotalPlannedAmount());

        return mapBudgetToBudgetResponse(budget);
    }

    private LocalDate adjustBudgetEndDate(CreateBudgetRequest createBudgetRequest) {
        LocalDate endDate;
        if (createBudgetRequest.getEndDate()==null){
            endDate = createBudgetRequest.getBudgetPeriodType().calculateEndDate(createBudgetRequest.getStartDate());
        }
        else{
            endDate = createBudgetRequest.getEndDate();
        }
        return endDate;
    }

    private BudgetResponse mapBudgetToBudgetResponse(Budget budget){
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setUserId(budget.getUserId());
        response.setName(budget.getName());
        response.setBudgetPeriodType(budget.getBudgetPeriodType());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setBudgetStatus(budget.getBudgetStatus());
        response.setTotalPlannedAmount(budget.getTotalPlannedAmount());
        response.setTotalActualSpent(budget.getTotalActualSpent());
        response.setTotalActualInvested(budget.getTotalActualInvested());
        response.setCategories(budget.getCategories());
        response.setCreatedAt(budget.getCreatedAt());
        response.setUpdatedAt(budget.getUpdatedAt());

        return response;
    }


}
