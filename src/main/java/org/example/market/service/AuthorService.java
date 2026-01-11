package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.AuthorDetailDto;
import org.example.market.dto.AuthorDto;
import org.example.market.dto.BookDetailDto;
import org.example.market.entity.Author;
import org.example.market.entity.Book;
import org.example.market.exception.NotFoundException;
import org.example.market.mapper.AuthorMapper;
import org.example.market.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorDetailDto getAuthorById(Long id) throws NotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        AuthorDetailDto authorDto = AuthorDetailDto.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .dateOfBirth(author.getDateOfBirth())
                .citizenship(author.getCitizenship())
                .languageOfWorks(author.getLanguageOfWorks())
                .build();

        List<Book> booksList = bookService.getBooksByAuthorId(id);
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
                                .dateOfBirth(author.getDateOfBirth())
                                .languageOfWorks(author.getLanguageOfWorks())
                                .citizenship(author.getCitizenship())
                                .build())
                        .build())
                .collect(Collectors.toList());

        authorDto.setBooks(bookDtos);

        return authorDto;
    }

    public AuthorDto addAuthor(AuthorDto dto)throws BadRequestException{
        Optional<Author> optionalAuthor = authorRepository.findByFirstNameAndLastName(
                dto.getFirstName(), dto.getLastName()
        );
        if (optionalAuthor.isPresent()) {
            throw new BadRequestException("Автор с таким ФИО уже существует");
        }
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setDateOfBirth(dto.getDateOfBirth());
        author.setCitizenship(dto.getCitizenship());
        author.setLanguageOfWorks(dto.getLanguageOfWorks());
        authorRepository.save(author);
        return AuthorMapper.INSTANCE.toDto(author);
    }

    public AuthorDto updateAuthor(Long id, AuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Автор не найден по айди"));

        AuthorMapper.INSTANCE.updateAuthorFromDto(dto, author);

        Author savedAuthor = authorRepository.save(author);

        return AuthorMapper.INSTANCE.toDto(savedAuthor);
    }
}
