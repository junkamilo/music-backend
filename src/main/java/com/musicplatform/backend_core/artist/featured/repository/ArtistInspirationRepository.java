package com.musicplatform.backend_core.artist.featured.repository;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.featured.entity.ArtistInspiration;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistInspirationRepository extends JpaRepository<ArtistInspiration, Long> {

    @Query("""
            SELECT ap FROM ArtistProfile ap
            JOIN ArtistInspiration ai ON ap.id = ai.emergentArtistId
            WHERE ai.featuredArtistId = :featuredArtistId
              AND ap.active = true
            ORDER BY ai.createdAt DESC
            """)
    List<ArtistProfile> findInspiredCreators(
            @Param("featuredArtistId") Long featuredArtistId,
            Pageable pageable
    );
}
