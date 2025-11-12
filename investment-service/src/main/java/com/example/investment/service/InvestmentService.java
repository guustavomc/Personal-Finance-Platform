package com.example.investment.service;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.exception.InvestmentNotFoundException;
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
                .orElseThrow(() ->new InvestmentNotFoundException(String.format("Failed to find investment with id %d", id)));

    }

    public InvestmentResponse saveInvestment(CreateInvestmentRequest createInvestmentRequest){
        Investment savedInvestment = saveVerifiedInvestment(createInvestmentRequest);
        return mapInvestmentToInvestmentResponse(savedInvestment);

    }

    private Investment saveVerifiedInvestment(CreateInvestmentRequest createInvestmentRequest){
        Investment investment = investmentRepository
                                .save(mapCreateInvestmentRequestToInvestment(createInvestmentRequest));

        return investment;
    }

    public void removeInvestment(long id){
        if(!investmentRepository.existsById(id)){
            throw new InvestmentNotFoundException(String.format("Failed to find investment with id %d", id));
        }
        else{
            removeVerifiedInvestment(id);
        }
    }

    private void removeVerifiedInvestment(long id){
        investmentRepository.deleteById(id);
    }

    public InvestmentResponse editInvestmentById(long id, CreateInvestmentRequest createInvestmentRequest){
        if(!investmentRepository.existsById(id)){
            throw new InvestmentNotFoundException(String.format("Failed to find investment with id %d", id));
        }
        else{
           Investment investment = editVerifiedInvestmentById(id, createInvestmentRequest);
            return mapInvestmentToInvestmentResponse(investment);
        }
        
    }

    private Investment editVerifiedInvestmentById(long id, CreateInvestmentRequest createInvestmentRequest){
        Investment investment = findVerifiedInvestmentTransactionWithID(id);

        investment.setInvestmentType(createInvestmentRequest.getInvestmentType());
        investment.setAssetSymbol(createInvestmentRequest.getAssetSymbol());
        investment.setAmountInvested(createInvestmentRequest.getAmountInvested());
        investment.setQuantity(createInvestmentRequest.getQuantity());
        investment.setInvestmentDate(createInvestmentRequest.getInvestmentDate());
        investment.setCurrency(createInvestmentRequest.getCurrency());
        investment.setAlternateAmount(createInvestmentRequest.getAlternateAmount());
        investment.setAlternateCurrency(createInvestmentRequest.getAlternateCurrency());

        return investmentRepository.save(investment);
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
