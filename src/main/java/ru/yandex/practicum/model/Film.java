package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Film {
    @NotNull
    private int id;
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @Length(max = 200, message = "Only 200 symbols are allowed")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive")
    private int duration;
}
