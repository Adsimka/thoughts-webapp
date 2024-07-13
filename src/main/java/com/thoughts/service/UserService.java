package com.thoughts.service;

import com.thoughts.dto.CreateUserDto;
import com.thoughts.exception.UserAlreadyExistException;
import com.thoughts.mapper.UserMapper;
import com.thoughts.model.User;
import com.thoughts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    @Value("${email.subject}") private final String subject;
    @Value("${email.messages}") private final String messages;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @SneakyThrows
    @Transactional
    public User registrationNewUser(CreateUserDto user) {
        if (emailExists(user.getEmail())) {
            String messageException = String.format("There is an account with that email address: %s", user.getEmail());
            throw new UserAlreadyExistException(messageException);
        }
        User saveUser = Optional.of(user)
                .map(userMapper::map)
                .map(userRepository::saveAndFlush)
                .orElseThrow();
        sendEmail(user);
        return saveUser;
    }

    @Transactional
    public Optional<User> update(Long id, User user) {
        return userRepository.findById(id)
                .map(entity -> map(user, entity))
                .map(userRepository::saveAndFlush);
    }

    private User map(User user, User entity) {
        if (user.getUsername() != null) {
            entity.setUsername(user.getUsername());
        }
        entity.setRoles(user.getRoles());

        return entity;
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email)
                .isPresent();
    }

    private void sendEmail(CreateUserDto user) {
        String token = getRandomToken();
        String confirmationUrl = String.format(messages, "http://localhost:8080/verify-email?token= %s", token);
        emailService.sendEmail(user.getEmail(), subject, confirmationUrl);
    }

    private String getRandomToken() {
        return UUID.randomUUID().toString();
    }
}
