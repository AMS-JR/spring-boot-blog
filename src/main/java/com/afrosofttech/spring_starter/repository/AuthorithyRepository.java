package com.afrosofttech.spring_starter.repository;

import com.afrosofttech.spring_starter.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorithyRepository extends JpaRepository<Authority, Long> {
}
