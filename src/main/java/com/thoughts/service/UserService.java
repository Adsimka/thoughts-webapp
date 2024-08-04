package com.thoughts.service;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.dto.user.ProfileUserDto;
import com.thoughts.dto.user.ReadUserDto;
import com.thoughts.exception.UserAlreadyExistException;
import com.thoughts.mapper.user.CreateUserMapper;
import com.thoughts.mapper.user.ProfileUserMapper;
import com.thoughts.mapper.user.ReadUserMapper;
import com.thoughts.model.Role;
import com.thoughts.model.User;
import com.thoughts.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService implements UserDetailsService {

    private final String subject;
    private final String messages;
    private final String verifyEmail;

    private final CreateUserMapper createUserMapper;
    private final ReadUserMapper readUserMapper;
    private final ProfileUserMapper profileUserMapper;

    private final UserRepository userRepository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(@Value("${email.subject}") String subject,
                       @Value("${email.messages}") String messages,
                       @Value("${link.verify}") String verifyEmail,
                       UserRepository userRepository,
                       CreateUserMapper createUserMapper,
                       ReadUserMapper readUserMapper,
                       ProfileUserMapper profileUserMapper,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.subject = subject;
        this.messages = messages;
        this.verifyEmail = verifyEmail;
        this.userRepository = userRepository;
        this.createUserMapper = createUserMapper;
        this.readUserMapper = readUserMapper;
        this.profileUserMapper = profileUserMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    @SneakyThrows
    @Transactional
    public void registrationNewUser(CreateUserDto user) {
        if (emailUsernameExists(user.getUsername(), user.getEmail())) {
            String exceptionMessage = String.format(
                    "There is an account with that username/email address: %s",
                    user.getEmail()
            );
            throw new UserAlreadyExistException(exceptionMessage);
        }
        String token = getRandomToken();
        Optional.of(user)
                .map(createUserMapper::map)
                .map(u -> {
                    u.setVerificationToken(token);
                    u.setRoles(Collections.singleton(Role.USER));
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    return u;
                })
                .map(userRepository::saveAndFlush)
                .orElseThrow();

        sendEmail(user, token);
    }

    @Transactional
    public Optional<ReadUserDto> updateUserByAdmin(Long id, User user) {
        return userRepository.findById(id)
                .map(entity -> map(user, entity))
                .map(userRepository::saveAndFlush)
                .map(readUserMapper::map);
    }

    @Transactional
    public Optional<ReadUserDto> updateProfile(Long id, CreateUserDto user) {
        return userRepository.findById(id)
                .map(u -> createUserMapper.map(user, u))
                .map(userRepository::saveAndFlush)
                .map(readUserMapper::map);
    }

    @Transactional
    public Optional<ProfileUserDto> profileFindById(Long id, Long currentUserId) {
        return userRepository.findById(id)
                .map(user -> {
                    boolean isSubscribed = user.getSubscribers().contains(currentUserId);
                    return profileUserMapper.map(user, isSubscribed);
                });
    }

    public Optional<ReadUserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(readUserMapper::map);
    }

    @Transactional
    public void subscribe(Long id, User currentUserId) {
        Optional<User> mbUser = userRepository.findById(id);
        if (mbUser.isPresent()) {
            User user = mbUser.get();
            user.getSubscribers().add(currentUserId);
            userRepository.save(user);
        }
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
        String confirmationUrl = String.format("%s - %s%s", messages, verifyEmail, token);
        emailService.sendEmail(user.getEmail(), subject, confirmationUrl);
    }

    private String getRandomToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }
}
