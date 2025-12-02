package org.studyeasy.SpringStarter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.SpringStarter.models.Account;
import org.studyeasy.SpringStarter.repositories.AccountRepository;

@Service
public class AccountService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) throws IllegalArgumentException {
        // Check if email already exists
        Optional<Account> existingAccount = accountRepository.findOneByEmailIgnoreCase(account.getEmail());
        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + account.getEmail());
        }
        
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole("ROLE_USER");
        }
        System.out.println("Saving account: " + account.getEmail() + " with role: " + account.getRole());
        return accountRepository.save(account);    
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + email);
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);
        if(!optionalAccount.isPresent()){
            System.out.println("Account not found for email: " + email);
            throw new UsernameNotFoundException("Account not found");
        }
        Account account = optionalAccount.get();
        System.out.println("Account found: " + account.getEmail() + ", Role: " + account.getRole());
        
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    }
    
}
