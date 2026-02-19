package com.budget.controller;

import com.budget.dto.BudgetResponse;
import com.budget.dto.CreateBudgetRequest;
import com.budget.model.Budget;
import com.budget.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@Validated
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping({"/{id}"})
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable("id") long id) {
        BudgetResponse response = new BudgetResponse();
        response = budgetService.findBudgetWithID(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping({"/{id}/refresh"})
    public ResponseEntity<BudgetResponse> getRefreshedBudgetById(@PathVariable("id") long id) {
        BudgetResponse response = new BudgetResponse();

        response = budgetService.findUpdatedBudgetWithID(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody CreateBudgetRequest createBudgetRequest) {
        BudgetResponse response = new BudgetResponse();
        try {
            response = budgetService.saveBudget(createBudgetRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> deleteBudget(@PathVariable("id") long id) {
        budgetService.removeBudgetWithID(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable("id") long id, @Valid @RequestBody CreateBudgetRequest createBudgetRequest) {
        BudgetResponse response = new BudgetResponse();
        response = budgetService.editBudgetByID(id, createBudgetRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}