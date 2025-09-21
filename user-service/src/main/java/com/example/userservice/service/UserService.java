package com.example.userservice.service;

import com.example.userservice.client.PointClient;
import com.example.userservice.domain.User;
import com.example.userservice.dto.AddActivityScoreRequestDto;
import com.example.userservice.dto.AddPointsRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .password(signUpRequestDto.getPassword())
                .build();
        User savedUser = userRepository.save(user);

        // 회원가입시 1000점 적립
        pointClient.addPoints(savedUser.getUserId(), 1000);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다!"));

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserResponseDto> getUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build()
                )
                .toList();
    }

    @Transactional
    public void addActivityScore(AddActivityScoreRequestDto addActivityScoreRequestDto) {
        User user = userRepository.findById(addActivityScoreRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다!"));

        user.addActivityScore(addActivityScoreRequestDto.getScore());

        userRepository.save(user);
    }
}
