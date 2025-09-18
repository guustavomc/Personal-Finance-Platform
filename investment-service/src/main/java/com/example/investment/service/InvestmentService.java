package com.example.investment.service;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentService {

    private InvestmentRepository investmentRepository;

    public InvestmentService (InvestmentRepository investmentRepository){
        this.investmentRepository=investmentRepository;
    }

    public List<InvestmentResponse> findAllInvestmentsMade(){
        return  investmentRepository.findAll().stream().map(investment -> mapInvestmentToInvestmentResponse(investment)).toList();
    }

    public InvestmentResponse findInvestmentTransactionWithID(Long id){
        Investment investment = findVerifiedInvestmentTransactionWithID(id);
        return mapInvestmentToInvestmentResponse(investment);
    }

    public Investment findVerifiedInvestmentTransactionWithID(Long id){
        return investmentRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Investment not found"));

    }

    public InvestmentResponse saveInvestment(CreateInvestmentRequest createInvestmentRequest){
        try {
            Investment savedInvestment = saveVerifiedExpense(createInvestmentRequest);
            return mapInvestmentToInvestmentResponse(savedInvestment);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to save Investments");
        }
    }

    private Investment saveVerifiedExpense(CreateInvestmentRequest createInvestmentRequest){
        Investment investment = investmentRepository
                                .save(mapCreateInvestmentRequestToInvestment(createInvestmentRequest));

        return investment;
    }


    public InvestmentResponse mapInvestmentToInvestmentResponse(Investment investment){
        InvestmentResponse response = new InvestmentResponse();
        response.setId(investment.getId());
        response.setInvestmentType(investment.getInvestmentType());
        response.setAssetSymbol(investment.getAssetSymbol());
        response.setAmountInvested(investment.getAmountInvested());
        response.setQuantity(investment.getQuantity());
        response.setInvestmentDate(investment.getInvestmentDate());
        response.setCurrency(investment.getCurrency());
        response.setAlternateAmount(investment.getAlternateAmount());
        response.setAlternateCurrency(investment.getAlternateCurrency());

        return response;
    }

    public Investment mapCreateInvestmentRequestToInvestment(CreateInvestmentRequest createInvestmentRequest){
        Investment investment = new Investment();
        investment.setInvestmentType(createInvestmentRequest.getInvestmentType());
        investment.setAssetSymbol(createInvestmentRequest.getAssetSymbol());
        investment.setAmountInvested(createInvestmentRequest.getAmountInvested());
        investment.setQuantity(createInvestmentRequest.getQuantity());
        investment.setInvestmentDate(createInvestmentRequest.getInvestmentDate());
        investment.setCurrency(createInvestmentRequest.getCurrency());
        investment.setAlternateAmount(createInvestmentRequest.getAlternateAmount());
        investment.setAlternateCurrency(createInvestmentRequest.getAlternateCurrency());

        return investment;
    }


}
