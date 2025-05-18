package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.entity.Post;
import com.afrosofttech.spring_starter.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;
    @Override
    public Optional<Post> getById(Long id){
        return postRepository.findById(id);
    }
    @Override
    public List<Post> getAll(){
        return postRepository.findAll();
    }
    @Override
    public void delete(Post post){
        postRepository.delete(post);
    }
    @Override
    public Post save(Post post){
//        if(post.getId() == null) post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
