package com.afrosofttech.spring_starter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Authority {
    @Id
    private Long id;
    private String name;
    @Version
    private Integer version;
}
