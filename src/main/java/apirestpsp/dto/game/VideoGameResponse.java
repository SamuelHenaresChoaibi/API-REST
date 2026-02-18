package apirestpsp.dto.game;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VideoGameResponse(
    Long id,
    String title,
    String platform,
    String genre,
    LocalDate releaseDate,
    BigDecimal rating,
    Long ownerId,
    String ownerEmail
) {
}
