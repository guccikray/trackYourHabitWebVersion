package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.EmailAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.UserNotFoundException;
import com.guccikray.trackYourHabits.exceptions.WrongInputDataException;
import com.guccikray.trackYourHabits.repositories.UserRepository;
import com.guccikray.trackYourHabits.userDetails.CustomUserDetails;
import com.guccikray.trackYourHabits.userEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public CustomUserDetails loadUserById(UUID id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(User user) throws EmailAlreadyExistsException {
        if (isEmailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("User with this email already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User sighIn(String email, String password) throws EmailAlreadyExistsException, WrongInputDataException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailAlreadyExistsException("User with this email doesn't exists"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongInputDataException("Incorrect password");
        }

        return user;
    }
}
