package ru.yandex.practicum.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.enums.GenreEnum;
import ru.yandex.practicum.enums.MpaEnum;
import ru.yandex.practicum.exceptions.NotFoundException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Set<Long> likes = new HashSet<>();

    @NotNull
    private long id;

    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    @Length(max = 200, message = "Разрешено 200 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private GenreEnum genre;

    private MpaEnum mpa;

    @Positive(message = "Продолжительность должна быть больше нуля")
    private int duration;

    /**
     * @param userId - id of user who liked the film
     */
    public void setLikes(long userId) throws NotFoundException {
        likes.add(userId);
    }

    public void removeLikes(long userId) throws NotFoundException {
        likes.remove(userId);
    }
}
