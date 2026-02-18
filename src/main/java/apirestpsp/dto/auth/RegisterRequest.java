package apirestpsp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    String name,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email invalido")
    @Size(max = 160, message = "El email no puede superar 160 caracteres")
    String email,

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 72, message = "La contrasena debe tener entre 6 y 72 caracteres")
    String password
) {
}
