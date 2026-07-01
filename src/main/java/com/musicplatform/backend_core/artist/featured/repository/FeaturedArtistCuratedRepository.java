package com.musicplatform.backend_core.artist.featured.repository;

import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCurated;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturedArtistCuratedRepository extends JpaRepository<FeaturedArtistCurated, Long> {

    @Query("""
            SELECT fc FROM FeaturedArtistCurated fc
            JOIN FETCH fc.featuredArtist fa
            WHERE fc.active = true
              AND fc.featuredFrom <= :date
              AND (fc.featuredUntil IS NULL OR fc.featuredUntil >= :date)
            ORDER BY fc.spotlightOrder ASC
            """)
    Page<FeaturedArtistCurated> findActiveForDate(@Param("date") LocalDate date, Pageable pageable);
}
