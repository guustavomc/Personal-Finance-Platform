package com.example.investment.controller;

import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.service.PortfolioSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/investment/portfolio")
@Validated
public class PortfolioSummaryController {

    @Autowired
    private PortfolioSummaryService portfolioSummaryService;

    @GetMapping("/summary")
    public ResponseEntity<PortfolioSummaryResponse> getSummary(){
        PortfolioSummaryResponse response = new PortfolioSummaryResponse();
        try {
            response = portfolioSummaryService.getPortfolioSummary();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }
    
}
