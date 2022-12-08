package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull
    private int id;
    @NotEmpty
    @NotBlank(message = "Name cant be empty")
    private String name;
    @Length(max = 200, message = "Only 200 symbols are allowed")
    private String description;
    @JsonFormat(pattern = "dd.MM.yyyy")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate releaseDate;
    @Positive(message = "The value must be positive")
    private int duration;
}
