package apirestpsp.dto.auth;

public record AuthResponse(String token, String type, long expiresInSeconds) {
}
