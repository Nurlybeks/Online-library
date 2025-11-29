package org.example.market.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.market.entity.Author;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookDetailDto {
    private String name;
    private AuthorDetailDto authorDetailDto;
    private int year;
    private String language;
    private double price;
    private String currency;
    private String description;
    private int quantity;
}
