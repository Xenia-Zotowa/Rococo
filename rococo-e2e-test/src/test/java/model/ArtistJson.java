package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public record ArtistJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("biography")
        String biography,
        @JsonProperty("photo")
        String photo
) {
    public ArtistJson(@Nonnull String name) {
        this(null, name, null, null);
    }

    public ArtistJson(@Nonnull String name, @Nonnull String biography) {
        this(null, name, biography, null);
    }

}
