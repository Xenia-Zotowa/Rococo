package io.student.rococo.data.repository;

import io.student.rococo.data.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<ArtistEntity, java.util.UUID> {
    @Query("SELECT a FROM ArtistEntity a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL ORDER BY a.name")
    Page<ArtistEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}