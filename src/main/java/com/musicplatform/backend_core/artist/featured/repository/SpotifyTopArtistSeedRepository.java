package com.musicplatform.backend_core.artist.featured.repository;

import com.musicplatform.backend_core.artist.featured.entity.SpotifyTopArtistSeed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyTopArtistSeedRepository extends JpaRepository<SpotifyTopArtistSeed, Long> {

    List<SpotifyTopArtistSeed> findAllByOrderBySyncPriorityAscStageNameAsc();
}
