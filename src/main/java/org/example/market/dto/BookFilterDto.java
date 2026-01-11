package org.example.market.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFilterDto {

    private String name;
    private Double minPrice;
    private Double maxPrice;
    private String language;
}
