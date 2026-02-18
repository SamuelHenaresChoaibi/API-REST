package apirestpsp.service;

import apirestpsp.dto.auth.AuthResponse;
import apirestpsp.dto.auth.LoginRequest;
import apirestpsp.dto.auth.RegisterRequest;
import apirestpsp.dto.auth.RegisterResponse;
import apirestpsp.entity.AppUser;
import apirestpsp.exception.BadRequestException;
import apirestpsp.repository.AppUserRepository;
import apirestpsp.security.JwtService;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        AppUserRepository appUserRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (appUserRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("El email ya esta registrado");
        }

        AppUser appUser = new AppUser();
        appUser.setName(request.name().trim());
        appUser.setEmail(email);
        appUser.setPassword(passwordEncoder.encode(request.password()));

        AppUser savedUser = appUserRepository.save(appUser);
        return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));

        String token = jwtService.generateToken(email);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
