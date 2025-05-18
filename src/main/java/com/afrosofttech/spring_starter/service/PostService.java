package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.entity.Post;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> getById(Long id);
    List<Post> getAll();
    void delete(Post post);
    Post save(Post post);
}
