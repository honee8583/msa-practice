package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .password(signUpRequestDto.getPassword())
                .build();
        userRepository.save(user);
    }
}
