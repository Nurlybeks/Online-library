package org.example.market.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.entity.Author;
import org.example.market.entity.Book;
import org.example.market.entity.Role;
import org.example.market.entity.User;
import org.example.market.exception.NotFoundException;
import org.example.market.repository.AuthorRepository;
import org.example.market.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthorRepository authorRepository;


    public void createUser(UserCreateDto dto) throws BadRequestException {
        User excistUser = userRepository.findByUsername(dto.getUsername()).orElse(null);
        if (excistUser != null) {
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setBirthdate(dto.getBirthDate());

        Role role = roleService.getUserRole();
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    public UserResponceDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        UserResponceDto dto = new UserResponceDto();
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setBirthDate(user.getBirthdate());

        return dto;
    }

    public void changePasswordByUser(ChangePasswordDto dto) throws BadRequestException {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new NotFoundException("Пользователь по указанному логину не найден"));
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Неверный пароль");
        }
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }


    public AuthorDetailDto getAuthorById(Long id) throws NotFoundException{
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