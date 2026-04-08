package io.student.rococo.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "museum")
public class MuseumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "binary(16) unique not null default (UUID_TO_BIN(UUID(), true))")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 255)
    private String city;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MuseumEntity)) return false;
        MuseumEntity that = (MuseumEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}