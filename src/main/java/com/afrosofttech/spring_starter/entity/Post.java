package com.afrosofttech.spring_starter.entity;

import com.afrosofttech.spring_starter.validation.RichTextNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    private String title;
//    @NotBlank
    @RichTextNotBlank(message = "Post body must not be blank")
    private String body;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName = "id", nullable = true)
    private Account account;

    @Version
    private Integer version;
}
