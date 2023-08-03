package com.example.service;

import com.example.model.entity.User;
import com.example.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findUserByEmail(username);
        if(!user.isPresent()){
            System.out.println("Here");
            throw new UsernameNotFoundException(username+ " not found");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail()).password(user.get().getPassword()).authorities("USER").build();
        return userDetails;
    }
}
