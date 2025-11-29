package org.example.market.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.example.market.entity.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthorDetailDto {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String citizenship;
    private String languageOfWorks;
    private List<BookDetailDto> books;
}
