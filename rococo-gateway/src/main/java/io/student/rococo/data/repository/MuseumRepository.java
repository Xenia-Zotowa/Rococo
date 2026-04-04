package io.student.rococo.data.repository;

import io.student.rococo.data.MuseumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MuseumRepository extends JpaRepository<MuseumEntity, java.util.UUID> {
    @Query("SELECT m FROM MuseumEntity m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL ORDER BY m.title")
    Page<MuseumEntity> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("SELECT m FROM MuseumEntity m WHERE LOWER(m.city) LIKE LOWER(CONCAT('%', :city, '%')) OR :city IS NULL ORDER BY m.id")
    Page<MuseumEntity> findByCityContainingIgnoreCase(@Param("city") String city, Pageable pageable);

    @Query("SELECT DISTINCT m FROM MuseumEntity m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')) AND m.country.id = :countryId ORDER BY m.title")
    Page<MuseumEntity> findWithCountryId(@Param("title") String title, @Param("countryId") java.util.UUID countryId, Pageable pageable);
}