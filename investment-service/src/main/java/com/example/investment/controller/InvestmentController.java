package com.example.investment.controller;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.service.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/investment")
@Validated
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @GetMapping
    public ResponseEntity<List<InvestmentResponse>> getAllInvestmentsMade(){
        List<InvestmentResponse> investmentResponses = new ArrayList<>();
        try {
            investmentResponses = investmentService.findAllInvestmentsMade();
            return ResponseEntity.status(HttpStatus.OK).body(investmentResponses);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(investmentResponses);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentResponse> getInvestmentById(@PathVariable("id") long id){
        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse = investmentService.findInvestmentTransactionWithID(id);
        return ResponseEntity.status(HttpStatus.OK).body(investmentResponse);
    }

    @PostMapping
    public ResponseEntity<InvestmentResponse> createInvestment(@Valid @RequestBody CreateInvestmentRequest createInvestmentRequest){
        InvestmentResponse investmentResponse = new InvestmentResponse();
        try {
            investmentResponse = investmentService.saveInvestment(createInvestmentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(investmentResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(investmentResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvestment(@PathVariable("id") long id){
        investmentService.removeInvestment(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestmentResponse> updateInvestment(@PathVariable("id") long id, @RequestBody CreateInvestmentRequest createInvestmentRequest){
        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse = investmentService.editInvestmentById(id, createInvestmentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(investmentResponse);
    }

}
