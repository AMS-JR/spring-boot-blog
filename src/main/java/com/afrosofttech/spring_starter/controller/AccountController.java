package com.afrosofttech.spring_starter.controller;

import com.afrosofttech.spring_starter.entity.Account;
import com.afrosofttech.spring_starter.service.AccountServiceImpl;
import com.afrosofttech.spring_starter.util.constants.AppUtil;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import javax.naming.Binding;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${password.token.reset.timeout.minutes}")
    private int passwordTokenTimeout;
//    @GetMapping("/register")
//    public String register(Model model){
//        Account account = new Account();
//        model.addAttribute("account", account);
//        return "account_views/register";
//    }
    @GetMapping("/register")
    public String register(Model model){
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", new Account());
        }
        return "account_views/register";
    }
    @PostMapping("/register")
    public String register_user(@Valid Account account,
                                BindingResult result, RedirectAttributes attr){
        if(result.hasErrors()){
            attr.addFlashAttribute("org.springframework.validation.BindingResult.account", result);
            attr.addFlashAttribute("account", account);
            return "redirect:/register";
        }
        accountService.save(account);
        return "redirect:/";
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model, Principal principal) {
        if (principal != null) {
            String authUser = principal.getName();
            Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                model.addAttribute("account", account);
                model.addAttribute("photo", account.getPhoto());
                return "account_views/profile";
            }
        }
        return "redirect:/?error";
    }
    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@Valid @ModelAttribute("account") Account account,
                                BindingResult result, Principal principal,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        if (result.hasErrors()) {
            return "account_views/profile";
        }

        String authUser = principal.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if (optionalAccount.isPresent()) {
            Account existingAccount = accountService.findById(optionalAccount.get().getId()).get();
            existingAccount.setFirstName(account.getFirstName());
            existingAccount.setLastName(account.getLastName());
            if (account.getPassword() != null && !account.getPassword().isEmpty()) {
                existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            }
            // Set other fields...

            accountService.save(existingAccount);
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return "redirect:/";
        } else {
            return "redirect:/home?error";
        }
    }
    @GetMapping("/login")
    public String login(Model model){
        return "account_views/login";
    }
    @PostMapping("/photo/update")
    @PreAuthorize("isAuthenticated()")
    public String updatePhoto(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes attributes,
            Principal principal) {
        // Implementation details
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded.");
            return "redirect:/profile";
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                String generatedString = RandomStringUtils.random(10, true, true);
                String finalPhotoName = generatedString + fileName;
                String fileLocation = AppUtil.getUploadPath(finalPhotoName);

                Path path = Paths.get(fileLocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                if (principal != null) {
                    String authUser = principal.getName();

                    Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                    if(optionalAccount.isPresent()){
                        Account account = optionalAccount.get();
                        Account existingAccount = accountService.findById(account.getId()).get();
                        String relativeFileLocation = "/uploads/" + finalPhotoName;
                        existingAccount.setPhoto(relativeFileLocation);
                        accountService.save(existingAccount);
                    }
                    //START-> for h2 database only. To be removed! allows immediate image ui update
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    //END->
                    return "redirect:/profile";
                }

            } catch (Exception e){
                attributes.addFlashAttribute("error", "Error uploading file.");
                return "redirect:/profile?error";
            }

        }
        return "redirect:/profile";
    }
    @GetMapping("/password/forgot")
    public String forgotPassword(Model model){
        return "account_views/forgot_password";
    }
    @PostMapping("/password/reset")
    public String resetPassword(@RequestParam("email") String _email,
                                RedirectAttributes attributes, Model model){
        Optional<Account> optionalAccount = accountService.findOneByEmail(_email);
        if(optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            // Generate and set token
            String resetToken = UUID.randomUUID().toString();
            account.setPasswordResetToken(resetToken);
            account.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordTokenTimeout));
            accountService.save(account);
            attributes.addFlashAttribute("message", "Password reset email sent");
            return "redirect:/login";
        } else {
            attributes.addAttribute("error", "No user found with the provided email.");
            return "redirect:/password/forgot";
        }
    }
}
