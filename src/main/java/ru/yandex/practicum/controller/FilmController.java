package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.AlreadyExistsException;
import ru.yandex.practicum.exceptions.DoesntExistException;
import ru.yandex.practicum.exceptions.InvalidDateInputException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("films")
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = film.getId();
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(EARLIEST_RELEASE_DATE)) {
            throw new InvalidDateInputException("Release date must be after 28 December 1895");
        }

        if (films.containsKey(id)) {
            throw new AlreadyExistsException("Film with id = " + id + " already exists.");
        }

        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, @RequestParam(value = "id", required = true) int id) {
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(EARLIEST_RELEASE_DATE)) {
            throw new InvalidDateInputException("Release date must be after 28 December 1895");
        }

        if (!films.containsKey(id)) {
            throw new DoesntExistException("Film with id = " + id + " does not exist.");
        }

        films.put(id, film);
        return film;
    }
}
