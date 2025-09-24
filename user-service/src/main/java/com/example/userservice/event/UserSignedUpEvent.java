package com.example.userservice.event;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSignedUpEvent {
    private Long userId;
    private String name;

}
