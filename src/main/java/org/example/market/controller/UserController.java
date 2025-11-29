package org.example.market.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.AuthorDetailDto;
import org.example.market.dto.BookDto;
import org.example.market.dto.UserResponceDto;
import org.example.market.exception.NotFoundException;
import org.example.market.service.AuthorService;
import org.example.market.service.BookService;
import org.example.market.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping("/me")
    public ResponseEntity<UserResponceDto> dto(Authentication authentication) {

        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        try {
            UserResponceDto dto = userService.findByUsername(authentication.getName());
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> dto() {
        try {
            List<BookDto> books = bookService.getListBooks();
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/author/{id}")
    public ResponseEntity<AuthorDetailDto> getAuthor(@PathVariable Long id) {
        try {
            AuthorDetailDto dto = authorService.getAuthorById(id);
            return ResponseEntity.ok(dto);
        } catch (NotFoundException notFoundException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
