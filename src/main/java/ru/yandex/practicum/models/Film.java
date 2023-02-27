package ru.yandex.practicum.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    @NotNull
    private long id;

    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    @Length(max = 200, message = "Разрешено 200 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private Genre[] genres;

    private Mpa mpa;

    @Positive(message = "Продолжительность должна быть больше нуля")
    private int duration;
}
