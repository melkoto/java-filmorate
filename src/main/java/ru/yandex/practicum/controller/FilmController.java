package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.AlreadyExistsException;
import ru.yandex.practicum.exceptions.DoesntExistException;
import ru.yandex.practicum.model.Film;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = film.getId();

        if (films.containsKey(film.getId())) {
            throw new AlreadyExistsException("Film with id = " + id + " already exists");
        }

        films.put(id, film);
        return film;
    }

    @PutMapping
    // public Film updateFilm(@Valid @RequestBody Film film) {
    public Film updateFilm(@Valid @RequestBody Film film, @RequestParam(value = "id", required = true) int id) {

        if (!films.containsKey(id)) {
            throw new DoesntExistException("Film with id = " + id + "does not exist");
        }

        films.put(id, film);
        return film;
    }
}
