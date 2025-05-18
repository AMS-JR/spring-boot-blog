package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.entity.Account;

import java.util.Optional;

public interface AccountService {
    Account save(Account account);
    Optional<Account> findOneByEmail(String email);
    Optional<Account> findById(Long id);
}
