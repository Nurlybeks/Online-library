package org.example.market.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.ChangePasswordDto;
import org.example.market.dto.UserCreateDto;
import org.example.market.entity.User;
import org.example.market.exception.NotFoundException;
import org.example.market.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDto dto) {
        try {
            userService.createUser(dto);
            return  ResponseEntity.ok("Пользователь успешно создан");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto) {
        try{
            userService.changePasswordByUser(dto);
            return ResponseEntity.ok("Пароль успешно обновлен");
        }catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
        }catch (BadRequestException badRequestException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestException.getMessage());
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
