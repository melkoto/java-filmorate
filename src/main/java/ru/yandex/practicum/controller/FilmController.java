package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.AlreadyExistsException;
import ru.yandex.practicum.exceptions.DoesntExistException;
import ru.yandex.practicum.exceptions.InvalidDateInputException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.utils.IdCreator;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = IdCreator.getId();
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(EARLIEST_RELEASE_DATE)) {
            throw new InvalidDateInputException("Release date must be after 28 December 1895");
        }

        if (films.containsKey(id)) {
            throw new AlreadyExistsException("Film with id = " + id + " already exists.");
        }

        film.setId(id);
        films.put(id, film);
        log.info("Film with id = {} is added to data", id);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int id = film.getId();
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(EARLIEST_RELEASE_DATE)) {
            throw new InvalidDateInputException("Release date must be after 28 December 1895");
        }

        if (!films.containsKey(id)) {
            throw new DoesntExistException("Film with id = " + id + " does not exist.");
        }

        films.put(id, film);
        log.info("Film with id = {} is updated", id);
        return film;
    }
}
