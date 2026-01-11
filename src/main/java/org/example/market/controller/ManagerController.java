package org.example.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.exception.NotFoundException;
import org.example.market.service.AuthorService;
import org.example.market.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping("/read")
    @Operation(summary = "API для получения всех книг", description = "Возвращает список книг и полное ФИО автора")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Статус успешного получения всех книг"), @ApiResponse(responseCode = "401", description = "Статус неверных авторизационных данных"), @ApiResponse(responseCode = "403", description = "Статус отсутствия прав для API"), @ApiResponse(responseCode = "500", description = "Внутренная ошибка сервера")})
    public ResponseEntity<Page<BookDto>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookDto> dto = bookService.getListBooks(pageable);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBook(@RequestBody BookUpdateDto dto) throws BadRequestException {
        BookUpdateDto bookDto = bookService.updateBook(dto);
        return ResponseEntity.ok("Изменение прошло успешно");
    }

    @GetMapping("/get/author/{id}")
    public ResponseEntity<AuthorDetailDto> getBookById(@PathVariable Long id) {
        try {
            AuthorDetailDto dto = authorService.getAuthorById(id);
            return ResponseEntity.ok(dto);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/book")
    public ResponseEntity<String> addBook(@RequestBody BookCreateDto dto) throws BadRequestException {
        bookService.addBook(dto);
        return ResponseEntity.ok("Книга успешно добавлена");
    }

    @PostMapping("/add/author")
    public ResponseEntity<String> addAuthor(@RequestBody AuthorDto dto) throws BadRequestException {
        AuthorDto authorDto = authorService.addAuthor(dto);
        return ResponseEntity.ok("Автор успешно добавлен");
    }

    @PutMapping("/update/author/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @RequestBody AuthorDto dto){
        AuthorDto response = authorService.updateAuthor(id, dto);
        return ResponseEntity.ok(response);
    }
}
