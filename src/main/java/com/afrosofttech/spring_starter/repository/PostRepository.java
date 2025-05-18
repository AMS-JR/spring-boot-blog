package com.afrosofttech.spring_starter.repository;

import com.afrosofttech.spring_starter.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
}
