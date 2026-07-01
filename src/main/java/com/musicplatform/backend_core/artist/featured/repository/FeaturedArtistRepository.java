package com.musicplatform.backend_core.artist.featured.repository;

import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturedArtistRepository extends JpaRepository<FeaturedArtist, Long> {

    Optional<FeaturedArtist> findBySpotifyId(String spotifyId);

    boolean existsBySpotifyId(String spotifyId);

    List<FeaturedArtist> findBySyncStatusIn(List<SyncStatus> syncStatuses);

    List<FeaturedArtist> findBySyncStatusOrderByPopularityDesc(SyncStatus syncStatus, Pageable pageable);
}
