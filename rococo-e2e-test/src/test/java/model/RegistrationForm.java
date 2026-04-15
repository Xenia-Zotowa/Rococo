package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistrationForm(
        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password,

        @JsonProperty("passwordSubmit")
        String passwordSubmit
) {
    // Конструктор для удобства, если нужно передать только username и password
    public RegistrationForm(String username, String password) {
        this(username, password, password);
    }
}
