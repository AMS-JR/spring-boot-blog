package com.afrosofttech.spring_starter.service.impl;

import com.afrosofttech.spring_starter.entity.Authority;
import com.afrosofttech.spring_starter.repository.AuthorithyRepository;
import com.afrosofttech.spring_starter.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorithyRepository authorithyRepository;
    @Override
    public Authority save(Authority authority){
        return authorithyRepository.save(authority);
    }
    @Override
    public Optional<Authority> findById(Long id){
        return authorithyRepository.findById(id);
    }
}
