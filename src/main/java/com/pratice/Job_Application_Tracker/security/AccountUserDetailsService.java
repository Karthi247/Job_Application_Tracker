package com.pratice.Job_Application_Tracker.security;

import com.pratice.Job_Application_Tracker.Repository.UserAccountRepository;
import com.pratice.Job_Application_Tracker.domain.entity.UserAccount;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {
    private final UserAccountRepository users;
    public AccountUserDetailsService(UserAccountRepository users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount account = users.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found."));
        return User.withUsername(account.getEmail()).password(account.getPasswordHash()).authorities("ROLE_USER").build();
    }
}
