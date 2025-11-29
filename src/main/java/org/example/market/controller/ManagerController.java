package org.example.market.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.AuthorDetailDto;
import org.example.market.dto.BookDto;
import org.example.market.dto.BookUpdateDto;
import org.example.market.service.AuthorService;
import org.example.market.service.BookService;
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
    public ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            List<BookDto> dto = bookService.getListBooks();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookUpdateDto dto) {
        try {
            BookUpdateDto bookUpdateDto = bookService.updateBook(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException badRequestException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/author/{id}")
    public ResponseEntity<AuthorDetailDto> getBookById(@PathVariable Long id) {
        try{
            AuthorDetailDto dto = authorService.getAuthorById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
