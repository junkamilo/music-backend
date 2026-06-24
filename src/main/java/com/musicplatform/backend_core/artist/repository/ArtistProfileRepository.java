package com.musicplatform.backend_core.artist.repository;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {

    Optional<ArtistProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Optional<ArtistProfile> findBySpotifyIdAndArtistTypeAndUserIdIsNull(
            String spotifyId,
            ArtistType artistType
    );

    Optional<ArtistProfile> findByStageNameIgnoreCaseAndArtistTypeAndUserIdIsNull(
            String stageName,
            ArtistType artistType
    );

    List<ArtistProfile> findByArtistTypeAndActiveTrueOrderByStageNameAsc(ArtistType artistType);
}
