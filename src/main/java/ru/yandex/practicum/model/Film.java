package ru.yandex.practicum.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull
    private int id;
    @NotEmpty
    private String name;
    private String description;
    @FutureOrPresent
    private LocalDate releaseDate;
    private int duration;
}
