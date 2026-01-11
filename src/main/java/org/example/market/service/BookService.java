package org.example.market.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.entity.Author;
import org.example.market.entity.Book;
import org.example.market.repository.AuthorRepository;
import org.example.market.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<BookDto> getListBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> {
                    String authorFullName = book.getAuthor().getFirstName()
                            + " " + book.getAuthor().getLastName();

                    return BookDto.builder()
                            .name(book.getName())
                            .authorFullName(authorFullName)
                            .year(book.getYear())
                            .language(book.getLanguage())
                            .price(book.getPrice())
                            .currency(book.getCurrency())
                            .description(book.getDescription())
                            .quantity(book.getQuantity())
                            .build();
                });
    }

    public BookDetailDto addBook(BookCreateDto bookCreateDto) throws BadRequestException {
        List<Book> isExist = bookRepository.findAllByName(bookCreateDto.getName());

        if (!isExist.isEmpty()) {
            throw new BadRequestException("Книга с названием " + bookCreateDto.getName() +
                    " уже существует");
        }

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

    public List<Book> getBooksByAuthorId(Long id) {
        return bookRepository.findAllByAuthorId(id);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<BookDto> dynamicSearch(BookFilterDto dto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String name =  "%" + dto.getName().toLowerCase().trim() + "%";
            predicates.add(cb.like(cb.lower(root.get("name")), name));
        }

        if(dto.getMinPrice() != null && dto.getMaxPrice() != null){
            predicates.add(cb.between(root.get("price"), dto.getMinPrice(), dto.getMaxPrice()));
        }else if(dto.getMinPrice() != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), dto.getMinPrice()));
        }else if (dto.getMaxPrice() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), dto.getMaxPrice()));
        }

        if (dto.getLanguage() != null && !dto.getLanguage().isBlank()) {
            String language = "%" + dto.getLanguage().toLowerCase().trim() + "%";
            predicates.add(cb.like(cb.lower(root.get("language")), language));
        }
        Predicate[] array = predicates.toArray(new Predicate[0]);
        cq.where(array);

        List<Book> books = entityManager.createQuery(cq).getResultList();

        return books.stream()
                .map(book -> BookDto.builder()
                        .name(book.getName())
                        .authorFullName(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName())
                        .price(book.getPrice())
                        .language(book.getLanguage())
                        .year(book.getYear())
                        .quantity(book.getQuantity())
                        .currency(book.getCurrency())
                        .description(book.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}