package com.example.investment.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;

@Service
public class PortfolioSummaryService {

    private InvestmentRepository investmentRepository;

    public PortfolioSummaryService(InvestmentRepository investmentRepository){
        this.investmentRepository=investmentRepository;
    }

    public PortfolioSummaryResponse getPortfolioSummary(){
        PortfolioSummaryResponse response = new PortfolioSummaryResponse();

        List<Investment> investments = investmentRepository.findAll();

        return response;
    }
    

    public AssetHolding mapInvestmentToHolding(Investment investment){
        AssetHolding holding = new AssetHolding();
        holding.setInvestmentType(investment.getInvestmentType());
        holding.setAssetSymbol(investment.getAssetSymbol());
        holding.setTotalAmountInvested(investment.getAlternateAmount());
        holding.setTotalQuantity(investment.getQuantity());
        holding.setPrimaryCurrency(investment.getCurrency());
        holding.setAlternateTotalAmountInvested(investment.getAlternateAmount());
        holding.setAlternateCurrency(investment.getAlternateCurrency());
        
        return holding;
    }
}
