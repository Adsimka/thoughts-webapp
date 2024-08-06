package com.thoughts.service;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.dto.user.ProfileUserDto;
import com.thoughts.dto.user.ReadUserDto;
import com.thoughts.exception.UserAlreadyExistException;
import com.thoughts.exception.UserNotFoundException;
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
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            throw new UserAlreadyExistException(String.format(
                    "There is an account with that username/email address: %s",
                    user.getEmail())
            );
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
                .map(entity -> updateExistingUser(user, entity))
                .map(userRepository::saveAndFlush)
                .map(readUserMapper::map);
    }

    @SneakyThrows
    @Transactional
    public Optional<ReadUserDto> updateProfile(Long id, CreateUserDto user) {
        return Optional.of(userRepository.findById(id)
                .map(existingUser -> updateUser(existingUser, user))
                .map(userRepository::save)
                .map(readUserMapper::map)
                .orElseThrow(() -> new UserNotFoundException(
                String.format("User not found: user id - %s", id)))
        );
    }

    @Transactional
    public Optional<ProfileUserDto> profileFindById(Long id, User currentUser) {
        return userRepository.findById(id)
                .map(user -> {
                    boolean isSubscribed = user.getSubscribers().contains(currentUser);
                    return profileUserMapper.map(user, isSubscribed);
                });
    }

    public Optional<ReadUserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(readUserMapper::map);
    }

    @Transactional
    public boolean subscribe(Long id, User currentUser) {
        return userRepository.findById(id)
                .map(user -> {
                    boolean isSubscribed = user.getSubscribers().add(currentUser);
                    userRepository.save(user);
                    return isSubscribed;
                })
                .orElse(false);
    }

    @Transactional
    public boolean unsubscribe(Long id, User currentUser) {
        return userRepository.findById(id)
                .map(user -> {
                    boolean isUnsubscribed = user.getSubscribers().remove(currentUser);
                    userRepository.save(user);
                    return isUnsubscribed;
                })
                .orElse(false);
    }

    private User updateUser(User existingUser, CreateUserDto user) {
        String email = user.getEmail();
        String token = getRandomToken();
        boolean isEmailChanged = emailService.isEmailChanged(email, existingUser.getEmail());

        if (!StringUtils.isEmpty(user.getPassword())) {
            existingUser.setPassword(user.getPassword());
        }
        if (!StringUtils.isEmpty(user.getUsername())
                && existingUser.getUsername() != user.getUsername()) {
            existingUser.setUsername(user.getUsername());
        }
        if (isEmailChanged) {
            existingUser.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                existingUser.setVerificationToken(token);
            }
        }

        if (isEmailChanged) {
            sendEmail(user, token);
        }

        return existingUser;
    }

    private User updateExistingUser(User user, User entity) {
        Optional.ofNullable(user.getUsername())
                .ifPresent(entity::setUsername);
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
