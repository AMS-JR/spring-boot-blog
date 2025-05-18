package com.afrosofttech.spring_starter.controller;

import com.afrosofttech.spring_starter.entity.Account;
import com.afrosofttech.spring_starter.entity.Post;
import com.afrosofttech.spring_starter.service.AccountService;
import com.afrosofttech.spring_starter.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model, Principal principal){
        Optional<Post> OptionalPost = postService.getById(id);
        String authUser = "email";
        if(OptionalPost.isPresent()){
            Post post = OptionalPost.get();
            model.addAttribute("post", post);
            if(principal != null){
                authUser = principal.getName();
            }
            if(authUser.equals(post.getAccount().getEmail())){
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }
            return "post_views/post";
        }
        return "404";
    }
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String showAddPostForm(Model model, Principal principal) {
        String authUser = principal.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_views/add_post";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String createPost(@Valid @ModelAttribute("post") Post post, BindingResult result, Principal principal) {
        if(result.hasErrors()){
            return "post_views/add_post";
        }
        String authUser = ""; // Default value

        if (principal != null) {
            authUser = principal.getName();
        }

        if (!post.getAccount().getEmail().equalsIgnoreCase(authUser)) {
            return "redirect:/?error";
        }

        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }
    @GetMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            model.addAttribute("post", optionalPost.get());
            return "post_views/edit_post";
        } else {
            return "404";
        }
    }
    @PostMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost( @Valid @ModelAttribute Post post, BindingResult result,@PathVariable Long id) {
        if(result.hasErrors()){
            return "post_views/edit_post";
        }
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);

        }
//        else {
//            throw new ResourceNotFoundException("Post not found with id " + id);
//        }
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            postService.delete(existingPost);
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }
}
