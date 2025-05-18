package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.entity.Authority;

import java.util.Optional;

public interface AuthorityService {

    Authority save(Authority authority);
    Optional<Authority> findById(Long id);
}
