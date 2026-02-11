package com.budget.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.budget.dto.BudgetResponse;
import com.budget.model.BudgetCategory;
import com.budget.model.BudgetPeriodType;
import com.budget.model.CategoryType;
import com.budget.service.BudgetService;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    @Test
    void getBudgetById_ReturnBudgetResponseWithRequestedID(){
        BudgetResponse budget = new BudgetResponse();
        budget.setId(1L);
        budget.setName("February");
        budget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budget.setStartDate(LocalDate.of(2026, 2, 1));
        budget.setEndDate(LocalDate.of(2026, 2, 28));
        budget.setTotalPlannedAmount(BigDecimal.valueOf(1000));

        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setPercentageOfTotal(BigDecimal.valueOf(50));

        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        budget.setCategories(categories);

        when(budgetService.findBudgetWithID(1L)).thenReturn(budget);

        ResponseEntity<BudgetResponse> budgetResponse = budgetController.getBudgetById(1L);
        assertEquals(HttpStatus.OK, budgetResponse.getStatusCode());
        assertEquals(1, budgetResponse.getBody().getId());

    }
    
}
