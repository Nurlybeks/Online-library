package org.example.market.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String name;
    private String authorFullName;
    private int year;
    private String language;
    private double price;
    private String currency;
    private String description;
    private int quantity;
}
