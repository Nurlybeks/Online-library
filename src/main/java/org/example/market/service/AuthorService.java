package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.example.market.dto.AuthorDetailDto;
import org.example.market.dto.BookDetailDto;
import org.example.market.entity.Author;
import org.example.market.entity.Book;
import org.example.market.exception.NotFoundException;
import org.example.market.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDetailDto getAuthorById(Long id) throws NotFoundException {
        Author author = authorRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        AuthorDetailDto authorDto = AuthorDetailDto.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .dateOfBirth(author.getDateOfBirth())
                .citizenship(author.getCitizenship())
                .languageOfWorks(author.getLanguageOfWorks())
                .build();

        List<Book> booksList = new ArrayList<>();
        List<BookDetailDto> bookDtos = booksList.stream()
                .map(book -> BookDetailDto.builder()
                        .name(book.getName())
                        .year(book.getYear())
                        .language(book.getLanguage())
                        .price(book.getPrice())
                        .currency(book.getCurrency())
                        .quantity(book.getQuantity())
                        .description(book.getDescription())
                        .authorDetailDto(AuthorDetailDto.builder()
                                .firstName(author.getFirstName())
                                .lastName(author.getLastName())
                                .build())
                        .build())
                .collect(Collectors.toList());

        authorDto.setBooks(bookDtos);

        return authorDto;
    }
}
