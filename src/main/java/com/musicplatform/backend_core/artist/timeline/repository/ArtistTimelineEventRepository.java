package com.musicplatform.backend_core.artist.timeline.repository;

import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEvent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistTimelineEventRepository extends JpaRepository<ArtistTimelineEvent, Long> {

    List<ArtistTimelineEvent> findByArtistIdOrderByEventDateAscOrderPositionAsc(Long artistId);

    Optional<ArtistTimelineEvent> findByIdAndArtistId(Long id, Long artistId);
}
