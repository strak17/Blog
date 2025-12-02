package org.studyeasy.SpringStarter.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.studyeasy.SpringStarter.models.Authority;
import org.studyeasy.SpringStarter.repositories.AccountRepository;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) throws IllegalArgumentException {

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole("USER");
        }

        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + email);
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);
        if (!optionalAccount.isPresent()) {
            System.out.println("Account not found for email: " + email);
            throw new UsernameNotFoundException("Account not found");
        }
        Account account = optionalAccount.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
        Set<Authority> authorities = new HashSet<>();
        for (Authority auth : authorities) {
            grantedAuthority.add(new SimpleGrantedAuthority(auth.getName()));
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    }

}
