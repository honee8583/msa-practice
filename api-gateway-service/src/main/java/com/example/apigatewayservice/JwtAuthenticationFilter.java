package com.example.apigatewayservice;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // Request Header 토큰 가져오기
            String token = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");
            System.out.println("토큰: " + token);

            // 토큰이 없을 경우 401
            if (token == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // SecretKey 토큰 검증 및 Paylaod(jserId) 가져오기
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            String subject = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            System.out.println("userId: " + subject);

            // userId를 X-User-Id 헤더에 담아서 다른 마이크로서비스에 전달
            return chain.filter(
                    exchange.mutate()
                            .request(
                                    exchange.getRequest()
                                            .mutate()
                                            .header("X-User-Id", subject)
                                            .build()
                            )
                            .build()
            );
        };
    }
}
