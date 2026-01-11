package org.example.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.exception.NotFoundException;
import org.example.market.service.AuthorService;
import org.example.market.service.BookService;
import org.example.market.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "AdminController", description = "API для админа")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
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

    @GetMapping("/get/author/{id}")
    @Operation(summary = "API для получения автора по айди", description = "Возвращает данные автора и все его книги")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Статус успешной обработки и получения данных"), @ApiResponse(responseCode = "404", description = "Статус отсутствия данных по айди автора"), @ApiResponse(responseCode = "500", description = "Внутренная ошибка сервера")})
    public ResponseEntity<AuthorDetailDto> getAuthor(@PathVariable Long id) throws BadRequestException {
        try {
            AuthorDetailDto dto = authorService.getAuthorById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (NotFoundException notFound) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @Operation(summary = "API для добавления книг", description = "Добавляет новую книгу в БД")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Статус успешного добавления книги"), @ApiResponse(responseCode = "400", description = "Статус некорректного запроса"), @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")})
    public ResponseEntity<BookDetailDto> addBook(@RequestBody BookCreateDto bookCreateDto) {
        try {
            BookDetailDto bookDetailDto = bookService.addBook(bookCreateDto);
            return new ResponseEntity<>(bookDetailDto, HttpStatus.CREATED);
        } catch (BadRequestException bd) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "API для обновления книги", description = "Обновляет книгу по ДТО")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Статус успешного обновления книги"), @ApiResponse(responseCode = "400", description = "Статус некорректного запроса"), @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")})
    public ResponseEntity<?> updateBook(@RequestBody BookUpdateDto dto) {
        try {
            BookUpdateDto bookUpdateDto = bookService.updateBook(dto);
            return ResponseEntity.ok().body("Книга с айди " + bookUpdateDto.getId() + " успешно обновлена");
        } catch (BadRequestException bd) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bd.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "API для удаления книги", description = "Удаляет книгу по ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Статус успешного удаления книги"), @ApiResponse(responseCode = "400", description = "Статус некорректного запроса"), @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")})
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Книга успешно удалена");
        } catch (BadRequestException bd) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bd);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/author")
    public ResponseEntity<String> addAuthor(@RequestBody AuthorDto dto) {
        try {
            authorService.addAuthor(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/change-password/user")
    public ResponseEntity<String> changePasswordUser(@RequestBody ChangePasswordUserDto dto){
        userService.changePasswordByUsername(dto);
        return ResponseEntity.ok().body("Пароль успешно обновлен");
    }
}

