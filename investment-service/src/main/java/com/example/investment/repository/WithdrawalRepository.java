package com.example.investment.repository;

import com.example.investment.model.Investment;
import com.example.investment.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByInvestmentType(String investmentType);

}
