package com.afrosofttech.spring_starter.controller;

import com.afrosofttech.spring_starter.entity.Post;
import com.afrosofttech.spring_starter.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model){
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts );
        return "home_views/home";
    }

}
