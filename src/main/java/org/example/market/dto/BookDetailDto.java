package org.example.market.dto;

import lombok.*;

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
