package apirestpsp.controller;

import apirestpsp.dto.game.VideoGameRequest;
import apirestpsp.dto.game.VideoGameResponse;
import apirestpsp.service.VideoGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Videojuegos")
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los videojuegos")
    public ResponseEntity<List<VideoGameResponse>> getAll() {
        return ResponseEntity.ok(videoGameService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver un videojuego por id")
    public ResponseEntity<VideoGameResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(videoGameService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un videojuego (requiere token)")
    public ResponseEntity<VideoGameResponse> create(@Valid @RequestBody VideoGameRequest request, Authentication authentication) {
        VideoGameResponse createdVideoGame = videoGameService.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideoGame);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un videojuego (requiere token y ser propietario)")
    public ResponseEntity<VideoGameResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody VideoGameRequest request,
        Authentication authentication
    ) {
        VideoGameResponse updatedVideoGame = videoGameService.update(id, request, authentication.getName());
        return ResponseEntity.ok(updatedVideoGame);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un videojuego (requiere token y ser propietario)")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        videoGameService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
