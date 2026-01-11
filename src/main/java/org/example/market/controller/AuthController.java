package org.example.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.market.dto.ChangePasswordDto;
import org.example.market.dto.UserCreateDto;
import org.example.market.exception.NotFoundException;
import org.example.market.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "API для управления учетками на стороне пользователя: регистация и смена пароля")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "API для регистрации пользователя", description = "Возвращает в случае успешной регистрации статус 201, иначе вернет ошибку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Статус успешной регистрации пользователя"),
            @ApiResponse(responseCode = "400", description = "Статус некорректного тела запроса"),
            @ApiResponse(responseCode = "500", description = "Статус в случае внутренней ошибки")
    })
    public ResponseEntity<?> register(@RequestBody UserCreateDto dto) {
        try {
            userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно создан");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "API для изменения пароля", description = "Принимает логин пользователя, его старый пароль и новый пароль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешной смены пароля"),
            @ApiResponse(responseCode = "404", description = "Статус если пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Статус некорректного тела запроса"),
            @ApiResponse(responseCode = "500", description = "Статус в случае внутренней ошибки")
    })
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto) {
        try {
            userService.changePasswordByUser(dto);
            return ResponseEntity.ok("Пароль успешно обновлен");
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
        } catch (BadRequestException badRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestException.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
