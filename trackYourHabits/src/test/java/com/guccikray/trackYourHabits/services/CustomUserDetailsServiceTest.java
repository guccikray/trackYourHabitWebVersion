package com.guccikray.trackYourHabits.services;

import com.guccikray.trackYourHabits.exceptions.EmailAlreadyExistsException;
import com.guccikray.trackYourHabits.exceptions.WrongInputDataException;
import com.guccikray.trackYourHabits.repositories.UserRepository;
import com.guccikray.trackYourHabits.userEntity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Test
    void isEmailExistsTest_returnFalse_whenEmailDoesntExists() {
        String email = "bob@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThat(customUserDetailsService.isEmailExists(email)).isFalse();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void isEmailExistsTest_returnsTrue_whenEmailExists () {
        String email = "bob@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThat(customUserDetailsService.isEmailExists(email)).isTrue();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void registerUserTest_throwsEmailAlreadyExistsException_whenEmailIsReserved() {
        User user = new User("bob", "bob@gmail.com", "qwerty1234");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> customUserDetailsService.registerUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with this email already exists");
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
    }

    @Test
    void registerUserTest_returnsUser_whenUserCreatedSuccessfully() throws EmailAlreadyExistsException {
        User user = new User("bob", "bob@gmail.com", "qwerty1234");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User newUser = customUserDetailsService.registerUser(user);

        assertThat(newUser.getName()).isEqualTo(user.getName());
        assertThat(newUser.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
    }

    @Test
    void signInTest_throwsEmailAlreadyExistsException_whenEmailIsIncorrect() {
        String email = "bob@gmail.com";
        String password = "qwerty1234";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.sighIn(email, password))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with this email doesn't exists");

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void signInTest_throwsWrongInputDataException_whenPasswordIsIncorrect() {
        String email = "bob@gmail.com";
        String password = "qwerty1234";
        User user = new User("bob", email, "qwerty4321");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> customUserDetailsService.sighIn(email, password))
                .isInstanceOf(WrongInputDataException.class)
                .hasMessage("Incorrect password");

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void signInTest_returnsUser_whenDataIsCorrect() throws EmailAlreadyExistsException, WrongInputDataException {
        String email = "bob@gmail.com";
        String password = "qwerty1234";
        User user = new User("bob", email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        User signedUser = customUserDetailsService.sighIn(email, password);
        assertThat(signedUser.getEmail()).isEqualTo(email);
        assertThat(signedUser.getPassword()).isEqualTo(password);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }
}
