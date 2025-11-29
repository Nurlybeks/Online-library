package org.example.market.entity;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private Author author;

    @Column(name = "YEAR", nullable = false, length = 4)
    private int year;

    @Column(name = "LANGUAGE", nullable = false)
    private String language;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "CURRENCY", nullable = false, length = 3)
    private String currency;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "QUANTITY", nullable = false, length = 10000)
    private int quantity;
}
