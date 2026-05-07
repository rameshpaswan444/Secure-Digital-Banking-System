package com.banking.Repository;

import com.banking.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TransactionRepository extends  JpaRepository<Transaction, Long>{

    Page<Transaction> findByAccountNumberOrderByTimestampDesc(
            String accountNumber,
            Pageable pageable
    );

    List<Transaction> findByType(String type);
}

