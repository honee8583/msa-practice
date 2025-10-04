package com.example.userservice.service;

import com.example.userservice.client.PointClient;
import com.example.userservice.domain.User;
import com.example.userservice.dto.AddActivityScoreRequestDto;
import com.example.userservice.dto.AddPointsRequestDto;
import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.LoginResponseDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.event.UserSignedUpEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointClient pointClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${jwt.secret}")
    private String jwtSecret;

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
        
        // 회원가입 완료 이벤트 발행
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .build();
        kafkaTemplate.send("user.signed-up", toJsonString(userSignedUpEvent));
    }

    private String toJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(object);
            return message;
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Json 직렬화 실패");
        }
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

//        throw new RuntimeException("활동 점수 적립 에러 발생");

        // 10초 대기
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT를 만들 때 사용하는 key 생성
        SecretKey secretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );

        // JWT 토큰 만들기
        String token = Jwts.builder()
                .subject(user.getUserId().toString())
                .signWith(secretKey)
                .compact();

        return new LoginResponseDto(token);
    }
}
