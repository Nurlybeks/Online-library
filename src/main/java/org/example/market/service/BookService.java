package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.entity.Author;
import org.example.market.entity.Book;
import org.example.market.mapper.BookMapper;
import org.example.market.repository.AuthorRepository;
import org.example.market.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookDto> getListBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> {

                    String authorFullName = book.getAuthor().getFirstName()
                            + " " + book.getAuthor().getLastName();

                    BookDto bookDto = BookDto.builder()
                            .name(book.getName())
                            .authorFullName(authorFullName)
                            .year(book.getYear())
                            .language(book.getLanguage())
                            .price(book.getPrice())
                            .currency(book.getCurrency())
                            .description(book.getDescription())
                            .quantity(book.getQuantity())
                            .build();
                    return bookDto;
                })
                .collect(Collectors.toList());
    }

    public BookDetailDto addBook(BookCreateDto bookCreateDto) throws BadRequestException {
        Author author = getAuthorById(bookCreateDto.getAuthorId());
        Book book = new Book();
        book.setName(bookCreateDto.getName());
        book.setAuthor(author);
        book.setYear(bookCreateDto.getYear());
        book.setLanguage(bookCreateDto.getLanguage());
        book.setPrice(bookCreateDto.getPrice());
        book.setCurrency(bookCreateDto.getCurrency());
        book.setDescription(bookCreateDto.getDescription());
        book.setQuantity(bookCreateDto.getQuantity());

        bookRepository.save(book);

        AuthorDetailDto authorDetailDto = AuthorDetailDto.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();

        BookDetailDto dto = new BookDetailDto();
        dto.setName(book.getName());
        dto.setAuthorDetailDto(authorDetailDto);
        dto.setYear(book.getYear());
        dto.setLanguage(book.getLanguage());
        dto.setPrice(book.getPrice());
        dto.setCurrency(book.getCurrency());
        dto.setDescription(book.getDescription());
        dto.setQuantity(book.getQuantity());
        return dto;
    }

    public BookUpdateDto updateBook(BookUpdateDto dto) throws BadRequestException {
        Book isExistBook = bookRepository.findById(dto.getId())
                .orElse(null);
        if (isExistBook == null) {
            throw new BadRequestException("Пользователь с таким айди не найден");
        }
        Author author = getAuthorById(dto.getAuthorId());
        Book books = new Book();
        books.setId(dto.getId());
        books.setName(dto.getName());
        books.setAuthor(author);
        books.setYear(dto.getYear());
        books.setLanguage(dto.getLanguage());
        books.setPrice(dto.getPrice());
        books.setCurrency(dto.getCurrency());
        books.setDescription(dto.getDescription());
        books.setQuantity(dto.getQuantity());

        bookRepository.save(books);

        return dto;
    }

    public Author getAuthorById(Long id) throws BadRequestException {
        return authorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Автор с таким айди не найден"));
    }

    public Book getBookById(Long id) throws BadRequestException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Книга не найдена по айди"));
    }

    public void deleteBook(Long id) throws BadRequestException {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}