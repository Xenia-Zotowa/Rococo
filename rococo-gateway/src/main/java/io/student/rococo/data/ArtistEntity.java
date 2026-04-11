package io.student.rococo.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "binary(16) unique not null default (UUID_TO_BIN(UUID(), true))")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 2000, nullable = false)
    private String biography;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistEntity)) return false;
        ArtistEntity that = (ArtistEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}