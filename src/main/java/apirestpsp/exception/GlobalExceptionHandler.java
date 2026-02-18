package apirestpsp.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiError> handleForbiddenOperation(ForbiddenOperationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::formatFieldError)
            .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Datos de entrada invalidos";
        }
        return buildError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales invalidas", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "No tienes permiso para esta operacion", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request);
    }

    private String formatFieldError(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage() == null ? "valor invalido" : fieldError.getDefaultMessage();
        return fieldError.getField() + ": " + defaultMessage;
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(apiError);
    }
}
