package org.example.market.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponceDto {
    private String username;
    private String fullName;
    private LocalDate birthDate;
}
