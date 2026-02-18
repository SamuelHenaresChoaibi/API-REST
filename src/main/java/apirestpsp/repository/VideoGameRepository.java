package apirestpsp.repository;

import apirestpsp.entity.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
}
