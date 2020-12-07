package com.codemountain.blogg.controller;

import com.codemountain.blogg.payload.request.LoginRequest;
import com.codemountain.blogg.payload.request.RegisterRequest;
import com.codemountain.blogg.payload.response.ApiResponse;
import com.codemountain.blogg.payload.response.JwtAuthenticationResponse;
import com.codemountain.blogg.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse apiResponse = authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity verifyAccount(@Valid @PathVariable String token) {
        authService.verifyAccount(token);
        log.info("Account verification successful");
        return ResponseEntity.status(HttpStatus.OK).body("Account verification successful");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("User login successful");
        return authService.loginUser(loginRequest);
    }

    //TODO -> Add api for refreshToken and logout
}
