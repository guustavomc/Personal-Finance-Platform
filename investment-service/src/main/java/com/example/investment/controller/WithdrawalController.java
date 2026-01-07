package com.example.investment.controller;

import com.example.investment.dto.CreateWithdrawalRequest;
import com.example.investment.dto.WithdrawalResponse;
import com.example.investment.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/investment/withdrawal")
@Validated
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping
    public ResponseEntity<List<WithdrawalResponse>> getAllWithdrawalsMade(){
        List<WithdrawalResponse> list = new ArrayList<>();
        try {
            list = withdrawalService.findAllWithdrawalsMade();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WithdrawalResponse> getWithdrawalWithId(@PathVariable("id") long id){
        WithdrawalResponse withdrawal = new WithdrawalResponse();
        withdrawal = withdrawalService.findWithdrawalById(id);
        return ResponseEntity.status(HttpStatus.OK).body(withdrawal);

    }

    @PostMapping
    public ResponseEntity<WithdrawalResponse> createWithdrawal(@Valid @RequestBody CreateWithdrawalRequest createWithdrawalRequest){
        WithdrawalResponse withdrawal = new WithdrawalResponse();
        withdrawal= withdrawalService.saveWithdrawal(createWithdrawalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(withdrawal);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWithdrawal(@PathVariable("id") long id){
        withdrawalService.removeWithdrawalWithID(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WithdrawalResponse> editWithdrawal(@PathVariable("id") long id, @Valid @RequestBody CreateWithdrawalRequest createWithdrawalRequest){
        WithdrawalResponse withdrawal = new WithdrawalResponse();
        withdrawal = withdrawalService.editWithdrawalWithID(id, createWithdrawalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(withdrawal);
    }

}
