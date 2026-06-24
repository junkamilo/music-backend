package com.musicplatform.backend_core.artist.milestone.repository;

import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistMilestoneRepository extends JpaRepository<ArtistMilestone, Long> {

    List<ArtistMilestone> findByArtistIdOrderByDateAchievedAscOrderPositionAsc(Long artistId);

    Optional<ArtistMilestone> findByIdAndArtistId(Long id, Long artistId);
}
