package com.afrosofttech.spring_starter.config;

import com.afrosofttech.spring_starter.entity.Account;
import com.afrosofttech.spring_starter.entity.Authority;
import com.afrosofttech.spring_starter.entity.Post;
import com.afrosofttech.spring_starter.service.impl.AccountServiceImpl;
import com.afrosofttech.spring_starter.service.AuthorityService;
import com.afrosofttech.spring_starter.service.PostService;
import com.afrosofttech.spring_starter.util.constants.Privilage;
import com.afrosofttech.spring_starter.util.constants.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private AuthorityService authorityService;
    @Override
    public void run(String... args) throws Exception {
        for(Privilage auth: Privilage.values()){
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getName());
            authorityService.save(authority);
        }
        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("amadou.asj.jallow@gmail.com");
        account01.setPassword("password");
        account01.setFirstName("user01");
        account01.setLastName("lastname01");

        account02.setEmail("admin@gmail.com");
        account02.setPassword("password");
        account02.setFirstName("admin");
        account02.setLastName("lastname02");
        account02.setRole(Role.ADMIN.getRole());

        account03.setEmail("editor@gmail.com");
        account03.setPassword("password");
        account03.setFirstName("editor");
        account03.setLastName("lastname03");
        account03.setRole(Role.EDITOR.getRole());

        account04.setEmail("super_editor@gmail.com");
        account04.setPassword("password");
        account04.setFirstName("super_editor");
        account04.setLastName("lastname04");
        account04.setRole(Role.EDITOR.getRole());

        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privilage.RESET_PASSWORD.getId()).ifPresent(authorities::add);
        authorityService.findById(Privilage.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);

        account04.setAuthorities(authorities);

        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);
        List<Post> posts = postService.getAll();
        if(posts.size() == 0){
            Post post01 = new Post();

            post01.setTitle("Welcome to the Blog");
            post01.setBody("This is the first post on our blog!");
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Spring Boot Tips");
            post02.setBody("Here are some useful tips for Spring Boot beginners.");
            post02.setAccount(account01);
            postService.save(post02);


        }
    }
}
