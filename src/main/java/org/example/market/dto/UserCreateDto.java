package org.example.market.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private String username;
    private String password;
    private String fullName;
    private LocalDate birthDate;
}
