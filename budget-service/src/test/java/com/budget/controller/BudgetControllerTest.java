package com.budget.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.budget.dto.BudgetCategoryRequest;
import com.budget.dto.BudgetItem;
import com.budget.dto.CreateBudgetRequest;
import com.budget.model.Budget;
import com.budget.service.BudgetItemService;
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

    @Mock
    private BudgetItemService budgetItemService;

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
        assertEquals(1L, budgetResponse.getBody().getId());

    }

    @Test
    void getRefreshedBudgetById_ReturnUpdatedBudgetResponse(){

        BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setId(1L);
        budgetResponse.setName("February");
        budgetResponse.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budgetResponse.setStartDate(LocalDate.of(2026, 2, 1));
        budgetResponse.setEndDate(LocalDate.of(2026, 2, 28));
        budgetResponse.setTotalPlannedAmount(BigDecimal.valueOf(1000));
        budgetResponse.setTotalActualSpent(BigDecimal.ZERO);
        budgetResponse.setTotalActualInvested(BigDecimal.ZERO);

        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setActualSpent(BigDecimal.ZERO);
        category1.setActualInvested(BigDecimal.ZERO);

        BudgetCategory category2 = new BudgetCategory();
        category2.setId(3L);
        category2.setCategoryName("Stock");
        category2.setType(CategoryType.INVESTMENT);
        category2.setPlannedAmount(BigDecimal.valueOf(1000));
        category2.setActualSpent(BigDecimal.ZERO);
        category2.setActualInvested(BigDecimal.ZERO);


        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        budgetResponse.setCategories(categories);


        when(budgetService.findUpdatedBudgetWithID(1L)).thenReturn(budgetResponse);

        ResponseEntity<BudgetResponse> response = budgetController.getRefreshedBudgetById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("February", response.getBody().getName());
        assertEquals(BudgetPeriodType.MONTHLY, response.getBody().getBudgetPeriodType());
        assertEquals(LocalDate.of(2026, 2, 1), response.getBody().getStartDate());
        assertEquals(LocalDate.of(2026, 2, 28), response.getBody().getEndDate());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getTotalPlannedAmount());

    }

    @Test
    void createBudget_FromCreateBudgetRequest_ReturnsBudgetResponse(){
        BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setId(1L);
        budgetResponse.setName("February");
        budgetResponse.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budgetResponse.setStartDate(LocalDate.of(2026, 2, 1));
        budgetResponse.setEndDate(LocalDate.of(2026, 2, 28));
        budgetResponse.setTotalPlannedAmount(BigDecimal.valueOf(1000));

        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setPercentageOfTotal(BigDecimal.valueOf(50));

        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        budgetResponse.setCategories(categories);

        CreateBudgetRequest createBudget = new CreateBudgetRequest();
        createBudget.setName("February");
        createBudget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        createBudget.setStartDate(LocalDate.of(2026, 2, 1));
        createBudget.setEndDate(LocalDate.of(2026, 2, 28));
        createBudget.setTotalPlannedAmount(BigDecimal.valueOf(1000));

        BudgetCategoryRequest createCategory1 = new BudgetCategoryRequest();
        createCategory1.setCategoryName("Food");
        createCategory1.setPlannedAmount(BigDecimal.valueOf(1000));
        createCategory1.setType(CategoryType.EXPENSE);
        createCategory1.setPlannedAmount(BigDecimal.valueOf(1000));
        createCategory1.setPercentageOfTotal(BigDecimal.valueOf(50));

        when(budgetService.saveBudget(createBudget)).thenReturn(budgetResponse);

        ResponseEntity<BudgetResponse> response = budgetController.createBudget(createBudget);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());

    }

    @Test
    void deleteBudget_ReturnResponseEntity_WithBudgetResponse(){
        long id = 1L;

        BudgetResponse budgetResponse = new BudgetResponse();

        doNothing().when(budgetService).removeBudgetWithID(id);
        ResponseEntity<String> response = budgetController.deleteBudget(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    
}
