package ru.yandex.practicum.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private int id;
    @Email
    private String email;
    @NotEmpty
    private String login;
    @NotEmpty
    private String name;
    @Past
    private LocalDate birthDate;
}
