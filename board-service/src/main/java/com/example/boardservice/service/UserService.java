package com.example.boardservice.service;

import com.example.boardservice.domain.User;
import com.example.boardservice.domain.UserRepository;
import com.example.boardservice.dto.SaveUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(SaveUserRequestDto saveUserRequestDto) {
        User user = User.builder()
                .userId(saveUserRequestDto.getUserId())
                .name(saveUserRequestDto.getName())
                .build();

        userRepository.save(user);
    }
}
