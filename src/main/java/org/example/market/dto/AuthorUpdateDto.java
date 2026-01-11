package org.example.market.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthorUpdateDto {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String citizenship;
    private String languageOfWorks;
}
