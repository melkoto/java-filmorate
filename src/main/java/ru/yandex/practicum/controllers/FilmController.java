package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = getId();
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        try {
            if (releaseDate.isBefore(EARLIEST_RELEASE_DATE)) {
                throw new ValidationException();
            }

            if (films.containsKey(id)) {
                throw new BadRequestException();
            }
        } catch (ValidationException ve) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Release date must be after 28 December 1895", ve
            );
        } catch (BadRequestException bre) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Film with id = " + id + " already exists.", bre
            );
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
            throw new ValidationException("Release date must be after 28 December 1895");
        }

        if (!films.containsKey(id)) {
            throw new BadRequestException("Film with id = " + id + " does not exist.");
        }

        films.put(id, film);
        log.info("Film with id = {} is updated", id);
        return film;
    }

    private int getId() {
        return id++;
    }
}
