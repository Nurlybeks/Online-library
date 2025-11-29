package org.example.market.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.*;
import org.example.market.exception.NotFoundException;
import org.example.market.service.AuthorService;
import org.example.market.service.BookService;
import org.example.market.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final AuthorService authorService;

    @GetMapping("/read")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            List<BookDto> dto = bookService.getListBooks();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/author/{id}")
    public ResponseEntity<AuthorDetailDto> getAuthor(@PathVariable Long id) throws BadRequestException {
        try{
            AuthorDetailDto dto = authorService.getAuthorById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (NotFoundException notFound){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<BookDetailDto> addBook(@RequestBody BookCreateDto bookCreateDto) {
        try {
            BookDetailDto bookDetailDto = bookService.addBook(bookCreateDto);
            return new ResponseEntity<>(bookDetailDto, HttpStatus.OK);
        } catch (BadRequestException bd) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBook(@RequestBody BookUpdateDto dto){
        try {
            BookUpdateDto bookUpdateDto = bookService.updateBook(dto);
            return ResponseEntity.ok().body("Книга с айди " +  bookUpdateDto.getId() + " успешно обновлена");
        }catch (BadRequestException bd){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bd.getMessage());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id){
        try{
            bookService.deleteBook(id);
            return ResponseEntity.ok("Книга успешно удалена");
        }catch (BadRequestException bd){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bd);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
