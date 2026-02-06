package com.budget.service;

import com.budget.dto.*;
import com.budget.exception.BudgetNotFoundException;
import com.budget.model.*;
import com.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetItemService budgetItemService;

    public BudgetResponse findBudgetWithID(Long id){
        Budget budget = findVerifiedBudgetWithID(id);
        return mapBudgetToBudgetResponse(budget);
    }

    public Budget findVerifiedBudgetWithID(Long id){
        return budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(String.format("Failed to find budget with id %d", id)));
    }


    public BudgetResponse saveBudget(CreateBudgetRequest createBudgetRequest){
        Budget budget = new Budget();
        budget.setName(createBudgetRequest.getName());
        budget.setBudgetPeriodType(createBudgetRequest.getBudgetPeriodType());
        budget.setStartDate(createBudgetRequest.getStartDate());

        budget.setEndDate(adjustBudgetEndDate(createBudgetRequest));

        budget.setBudgetStatus(BudgetStatus.ACTIVE);

        budget.setTotalPlannedAmount(createBudgetRequest.getTotalPlannedAmount());

        mapBudgetCategoryRequestToNewBudget(createBudgetRequest, budget);

        return mapBudgetToBudgetResponse(budgetRepository.save(budget));
    }

    private void updateBudgetCategoryActualValues(Long budgetId){
        Budget budget = findVerifiedBudgetWithID(budgetId);

        budget.setTotalActualSpent(BigDecimal.ZERO);
        budget.setTotalActualInvested(BigDecimal.ZERO);

        for(BudgetCategory category: budget.getCategories()){
            category.setActualSpent(BigDecimal.ZERO);
            category.setActualInvested(BigDecimal.ZERO);
        }

        List<BudgetItem> items = budgetItemService.getBudgetItemsForSpecificBudget(budget);

        for(BudgetItem item: items){
            BudgetCategory matchingCategory = findMatchingCategory(budget, item);
            if(matchingCategory != null){
                if (matchingCategory.getType().equals(CategoryType.EXPENSE)){
                    BigDecimal newExpense = matchingCategory.getActualSpent().add(item.getAmount());
                    matchingCategory.setActualSpent(newExpense);

                    budget.setTotalActualSpent(budget.getTotalActualSpent().add(item.getAmount()));
                }
                if (matchingCategory.getType().equals(CategoryType.INVESTMENT)){
                    BigDecimal newInvestment = matchingCategory.getActualInvested().add(item.getAmount());
                    matchingCategory.setActualInvested(newInvestment);

                    budget.setTotalActualInvested(budget.getTotalActualInvested().add(item.getAmount()));
                }
            }
        }

        budget.setUpdatedAt(LocalDateTime.now());
        budgetRepository.save(budget);

    }

    private BudgetCategory findMatchingCategory(Budget budget, BudgetItem item){
        return budget.getCategories().stream()
                .filter(category -> category.getType() == item.getType())
                .filter(category -> category.getCategoryName().equals(item.getBudgetItemCategory()))
                .findFirst()
                .orElse(null);

    }

    private static void mapBudgetCategoryRequestToNewBudget(CreateBudgetRequest createBudgetRequest, Budget budget) {
        for(BudgetCategoryRequest category: createBudgetRequest.getCategories()){
            BudgetCategory newCategory = new BudgetCategory();
            newCategory.setCategoryName(category.getCategoryName());
            newCategory.setType(category.getType());
            newCategory.setPlannedAmount(category.getPlannedAmount());
            newCategory.setPercentageOfTotal(category.getPercentageOfTotal() != null ?
                    category.getPercentageOfTotal() : BigDecimal.ZERO);

            budget.addCategory(newCategory);
        }
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
