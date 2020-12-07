package com.codemountain.blogg.service;

import com.codemountain.blogg.exception.BadRequestException;
import com.codemountain.blogg.exception.ResourceNotFoundException;
import com.codemountain.blogg.model.VerificationToken;
import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.payload.request.LoginRequest;
import com.codemountain.blogg.payload.request.MailRequest;
import com.codemountain.blogg.payload.request.RegisterRequest;
import com.codemountain.blogg.payload.response.ApiResponse;
import com.codemountain.blogg.payload.response.JwtAuthenticationResponse;
import com.codemountain.blogg.repository.UserRepository;
import com.codemountain.blogg.repository.VerificationTokenRepository;
import com.codemountain.blogg.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.codemountain.blogg.utils.Constant.*;

@AllArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ApiResponse registerUser(RegisterRequest registerRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername()))) {
            throw new BadRequestException("Username is already taken");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerRequest.getEmail()))) {
            throw new BadRequestException("Email is already taken");
        }

        String firstName = registerRequest.getFirstName().toLowerCase();
        String lastName = registerRequest.getLastName().toLowerCase();
        String email = registerRequest.getEmail().toLowerCase();
        String password = passwordEncoder.encode(registerRequest.getPassword());
        String username = registerRequest.getUsername().toLowerCase();

        User user = new User(firstName, lastName, username, password, email, false);
        userRepository.save(user);

        String token = generateUserToken(user);
        mailService.sendMail(new MailRequest(user.getEmail(),
                "Please Activate your account",
                "Thank you for joining Blogg, " + "Please click on the link below to activate your account : " +
                        "http://localhost:8080/api/v1/auth/accountVerification/" + token));

        return new ApiResponse(Boolean.TRUE, "User Account created successfully");
    }

    /**
     *This generate a random UUID that would be persisted in the
     * database just in case the user does not confirm the link
     * immediately.
     */
    private String generateUserToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new BadRequestException("Invalid token"));
        fetchAndEnableUser(verificationToken.get());
    }


    /**
     * Method called only when the user has successfully
     * activated their email address
     * After account activation delete the token from database
     */
    @Transactional
    void fetchAndEnableUser(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(EMAIL, USER_NOT_FOUND_WITH_EMAIL, email));
        user.setUserIsEnabled(true);

        userRepository.save(user);
        verificationTokenRepository.deleteById(verificationToken.getId());
    }

    @Transactional
    public ResponseEntity<JwtAuthenticationResponse> loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
