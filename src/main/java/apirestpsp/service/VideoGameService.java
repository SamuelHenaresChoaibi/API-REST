package apirestpsp.service;

import apirestpsp.dto.game.VideoGameRequest;
import apirestpsp.dto.game.VideoGameResponse;
import apirestpsp.entity.AppUser;
import apirestpsp.entity.VideoGame;
import apirestpsp.exception.ForbiddenOperationException;
import apirestpsp.exception.ResourceNotFoundException;
import apirestpsp.repository.AppUserRepository;
import apirestpsp.repository.VideoGameRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoGameService {

    private final VideoGameRepository videoGameRepository;
    private final AppUserRepository appUserRepository;

    public VideoGameService(VideoGameRepository videoGameRepository, AppUserRepository appUserRepository) {
        this.videoGameRepository = videoGameRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public List<VideoGameResponse> getAll() {
        return videoGameRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public VideoGameResponse getById(Long id) {
        VideoGame videoGame = videoGameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Videojuego no encontrado con id " + id));
        return toResponse(videoGame);
    }

    @Transactional
    public VideoGameResponse create(VideoGameRequest request, String ownerEmail) {
        AppUser owner = getUserByEmail(ownerEmail);

        VideoGame videoGame = new VideoGame();
        applyRequest(videoGame, request);
        videoGame.setOwner(owner);

        return toResponse(videoGameRepository.save(videoGame));
    }

    @Transactional
    public VideoGameResponse update(Long id, VideoGameRequest request, String ownerEmail) {
        VideoGame videoGame = videoGameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Videojuego no encontrado con id " + id));

        ensureOwner(videoGame, ownerEmail);
        applyRequest(videoGame, request);

        return toResponse(videoGameRepository.save(videoGame));
    }

    @Transactional
    public void delete(Long id, String ownerEmail) {
        VideoGame videoGame = videoGameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Videojuego no encontrado con id " + id));

        ensureOwner(videoGame, ownerEmail);
        videoGameRepository.delete(videoGame);
    }

    private AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private void ensureOwner(VideoGame videoGame, String ownerEmail) {
        if (!videoGame.getOwner().getEmail().equalsIgnoreCase(ownerEmail)) {
            throw new ForbiddenOperationException("No puedes modificar o borrar videojuegos de otro usuario");
        }
    }

    private void applyRequest(VideoGame videoGame, VideoGameRequest request) {
        videoGame.setTitle(request.title().trim());
        videoGame.setPlatform(request.platform().trim());
        videoGame.setGenre(request.genre().trim());
        videoGame.setReleaseDate(request.releaseDate());
        videoGame.setRating(request.rating());
    }

    private VideoGameResponse toResponse(VideoGame videoGame) {
        return new VideoGameResponse(
            videoGame.getId(),
            videoGame.getTitle(),
            videoGame.getPlatform(),
            videoGame.getGenre(),
            videoGame.getReleaseDate(),
            videoGame.getRating(),
            videoGame.getOwner().getId(),
            videoGame.getOwner().getEmail()
        );
    }
}
