package com.example.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpRequestDto {

    private String email;

    private String name;

    private String password;

}
