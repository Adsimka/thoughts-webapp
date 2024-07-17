package com.thoughts.service;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.exception.UserAlreadyExistException;
import com.thoughts.mapper.CreateUserMapper;
import com.thoughts.model.User;
import com.thoughts.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService implements UserDetailsService {

    private final String subject;
    private final String messages;

    private final UserRepository userRepository;
    private final CreateUserMapper createUserMapper;
    private final EmailService emailService;

    public UserService(@Value("${email.subject}") String subject,
                       @Value("${email.messages}") String messages,
                       UserRepository userRepository,
                       CreateUserMapper createUserMapper,
                       EmailService emailService) {
        this.subject = subject;
        this.messages = messages;
        this.userRepository = userRepository;
        this.createUserMapper = createUserMapper;
        this.emailService = emailService;
    }

    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    @SneakyThrows
    @Transactional
    public void registrationNewUser(CreateUserDto user) {
        if (emailUsernameExists(user.getUsername(), user.getEmail())) {
            String exceptionMessage = String.format("There is an account with that username/email address: %s",
                    user.getEmail());
            log.info(exceptionMessage);

            throw new UserAlreadyExistException(exceptionMessage);
        }
        String token = getRandomToken();
        User saveUser = Optional.of(user)
                .map(createUserMapper::map)
                .map(u -> {
                    u.setVerificationToken(token);
                    return u;
                })
                .map(userRepository::saveAndFlush)
                .orElseThrow();
        log.info("The user with the {} has been successfully added to the system (without activation)", user.getEmail());

        sendEmail(user, token);
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

    private boolean emailUsernameExists(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email)
                .isPresent();
    }

    private void sendEmail(CreateUserDto user, String token) {
//        TODO 14.07.2024 Fix String concat
        String confirmationUrl = messages + " http://localhost:8080/registration/verify-email?token=" + token;
        emailService.sendEmail(user.getEmail(), subject, confirmationUrl);
    }

    private String getRandomToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }
}
