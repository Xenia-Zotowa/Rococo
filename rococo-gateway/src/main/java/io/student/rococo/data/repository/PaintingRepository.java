package io.student.rococo.data.repository;

import io.student.rococo.data.PaintingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {
    @Query("SELECT p FROM PaintingEntity p ORDER BY p.title")
    Page<PaintingEntity> findAll(Pageable pageable);

    @Query("SELECT p FROM PaintingEntity p WHERE p.artist.id = :artistId ORDER BY p.title")
    List<PaintingEntity> findByArtistId(
            @Param("artistId") UUID artistId
    );
}