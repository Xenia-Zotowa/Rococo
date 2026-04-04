package io.student.rococo.data.repository;

import io.student.rococo.data.CountryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
    @Query("SELECT c FROM CountryEntity c ORDER BY c.name")
    List<CountryEntity> findAllOrdered(Pageable pageable);
}