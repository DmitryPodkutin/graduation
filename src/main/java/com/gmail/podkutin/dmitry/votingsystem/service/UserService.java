package com.gmail.podkutin.dmitry.votingsystem.service;

import com.gmail.podkutin.dmitry.votingsystem.AuthorizedUser;
import com.gmail.podkutin.dmitry.votingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.gmail.podkutin.dmitry.votingsystem.model.user.User;
import com.gmail.podkutin.dmitry.votingsystem.util.exception.NotFoundException;


@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService, AuthenticationManager {

    protected final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase()).orElseThrow(() -> new NotFoundException(" Not found user with (method loadUserByUsername) "));
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        log.info("loadUserByUsername " + email);
        return new AuthorizedUser(user);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }
}


