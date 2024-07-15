package com.thoughts.service;

import com.thoughts.model.User;
import com.thoughts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final UserRepository userRepository;

    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
    }

    public String validateVerificationToken(String token) {
        var user = userRepository
                .findByVerificationToken(token)
                .orElse(null);
        if (user == null) {
            return "invalid";
        }
        user.setActive(true);
        userRepository.save(user);

        return "valid";
    }
}
