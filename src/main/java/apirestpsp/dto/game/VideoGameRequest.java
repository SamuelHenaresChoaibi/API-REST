package apirestpsp.dto.game;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record VideoGameRequest(
    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 120, message = "El titulo no puede superar 120 caracteres")
    String title,

    @NotBlank(message = "La plataforma es obligatoria")
    @Size(max = 80, message = "La plataforma no puede superar 80 caracteres")
    String platform,

    @NotBlank(message = "El genero es obligatorio")
    @Size(max = 80, message = "El genero no puede superar 80 caracteres")
    String genre,

    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    LocalDate releaseDate,

    @NotNull(message = "La puntuacion es obligatoria")
    @DecimalMin(value = "0.0", message = "La puntuacion minima es 0.0")
    @DecimalMax(value = "10.0", message = "La puntuacion maxima es 10.0")
    @Digits(integer = 2, fraction = 1, message = "La puntuacion debe tener maximo un decimal")
    BigDecimal rating
) {
}
