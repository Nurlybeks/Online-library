package org.example.market.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDto {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String citizenship;
    private String languageOfWorks;
}
