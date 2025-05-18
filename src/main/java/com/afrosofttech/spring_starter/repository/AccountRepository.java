package com.afrosofttech.spring_starter.repository;

import com.afrosofttech.spring_starter.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findOneByEmailIgnoreCase(String email);
}