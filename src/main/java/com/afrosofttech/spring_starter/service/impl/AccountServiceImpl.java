package com.afrosofttech.spring_starter.service.impl;

import com.afrosofttech.spring_starter.dto.BlogOperationPerformedEvent;
import com.afrosofttech.spring_starter.entity.Account;
import com.afrosofttech.spring_starter.entity.Authority;
import com.afrosofttech.spring_starter.repository.AccountRepository;
import com.afrosofttech.spring_starter.security.KafkaEventProducerService;
import com.afrosofttech.spring_starter.service.AccountService;
import com.afrosofttech.spring_starter.util.constants.OperationType;
import com.afrosofttech.spring_starter.util.constants.Role;
import ognl.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService,UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaEventProducerService kafkaEventProducerService;
//    @Value("${spring.image.files}")
//    private String photo_prefix;

//    public List<Account> getAll(){
//        return accountRepository.findAll();
//    }
//    public void delete(Account account){
//        accountRepository.delete(account);
//    }
    @Override
    public Account save(Account account) {
        Optional<Account> existingAccountOpt = account.getId() != null
                ? accountRepository.findById(account.getId())
                : Optional.empty();

        // Check if we're creating a new account or updating
        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            // Only encode password if it's different from the existing one
            if (!account.getPassword().equals(existingAccount.getPassword())) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
            }
        } else {
            // New account, encode password
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        if (account.getRole() == null) {
            account.setRole(Role.USER.getRole());
        }

        if (account.getPhoto() == null) {
            account.setPhoto("/images/person.png");
        }
        Account savedAccount = accountRepository.save(account);

        BlogOperationPerformedEvent blogOperationPerformedEvent =
                BlogOperationPerformedEvent.builder().operationType(OperationType.USER_CREATED.name())
                .userId(savedAccount.getEmail()).entityId(savedAccount.getId().toString())
                        .entityType(OperationType.USER_CREATED.name())
                                .timestamp(LocalDateTime.now())
                .details("From frontend").build();
        kafkaEventProducerService.publishBlogOperationsPerformedEvent(blogOperationPerformedEvent);
        return savedAccount;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user: " + email);
        System.out.println("--------------------------------------------------------");
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }
        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(account.getRole()));
        for(Authority auth: account.getAuthorities()){
            grantedAuthorities.add(new SimpleGrantedAuthority(auth.getName()));
        }
        return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }
    @Override
    public Optional<Account> findOneByEmail(String email){
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
    @Override
    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }
    @Override
    public Optional<Account> findByToken(String token){
        return accountRepository.findByPasswordResetToken(token);
    }
    @Override
    public void updatePassword(Account account) {
        Account accountById = accountRepository.findById(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        accountById.setPassword(passwordEncoder.encode(account.getPassword()));
        accountById.setPasswordResetToken(null);
        accountById.setPasswordResetTokenExpiry(null);
        accountRepository.save(accountById);
    }
}