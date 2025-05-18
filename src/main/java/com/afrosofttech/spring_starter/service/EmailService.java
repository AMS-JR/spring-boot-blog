package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.dto.EmailDto;

public interface EmailService {
    Boolean sendSimpleMail(EmailDto emailDto);
    void sendPasswordResetEmail(String to, String token);
}
