package org.studyeasy.SpringBlog.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringBlog.models.Account;
import org.studyeasy.SpringBlog.models.Authority;
import org.studyeasy.SpringBlog.models.Post;
import org.studyeasy.SpringBlog.repositories.AccountRepository;
import org.studyeasy.SpringBlog.services.AccountService;
import org.studyeasy.SpringBlog.services.AuthorityService;
import org.studyeasy.SpringBlog.services.PostService;
import org.studyeasy.SpringBlog.util.constants.Privillages;
import org.studyeasy.SpringBlog.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (accountRepository.count() > 0) {
            System.out.println("Accounts already exist. Skipping seed data.");
            return;
        }

        // Seed Authorities
        for (Privillages auth : Privillages.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getPrivillage());
            authorityService.save(authority);
        }

        // Create Accounts
        Account account01 = new Account();
        account01.setEmail("user@mail.com");
        account01.setPassword("pass123");
        account01.setFirstname("User");
        account01.setLastname("Lastname");

        Account account02 = new Account();
        account02.setEmail("admin@mail.com");
        account02.setPassword("pass123");
        account02.setFirstname("Admin");
        account02.setLastname("Lastname");
        account02.setRole(Roles.ADMIN.getRole());

        Account account03 = new Account();
        account03.setEmail("editor@mail.com");
        account03.setPassword("pass123");
        account03.setFirstname("Editor");
        account03.setLastname("Lastname");
        account03.setRole(Roles.EDITOR.getRole());

        Account account04 = new Account();
        account04.setEmail("super_editor@mail.com");
        account04.setPassword("pass123");
        account04.setFirstname("Super");
        account04.setLastname("Editor");
        account04.setRole(Roles.EDITOR.getRole());

        // Assign specific authorities to super editor
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privillages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        authorityService.findById(Privillages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);

        // Save accounts
        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);

        // Seed Posts
        if (postService.getAll().isEmpty()) {
            Post post01 = new Post();
            post01.setTitle("Post 01");
            post01.setBody("Post 01 body...");
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Post 02");
            post02.setBody("Post 02 body...");
            post02.setAccount(account02);
            postService.save(post02);
        }
    }
}
