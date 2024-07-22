package com.thoughts.service;

import com.thoughts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {

    private final UserRepository userRepository;

    public boolean validateVerificationToken(String token) {
        var user = userRepository
                .findByVerificationToken(token)
                .orElse(null);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);

        return true;
    }
}
