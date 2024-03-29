package ru.yandex.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.services.film.FilmService;
import ru.yandex.practicum.utils.Constants;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(Constants.EARLIEST_RELEASE_DATE)) {
            throw new BadRequestException("Дата выпуска должна быть не ранее 28 Декабря 1895.");
        }

        return filmService.addFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        LocalDate releaseDate = LocalDate.parse(String.valueOf(film.getReleaseDate()));

        if (releaseDate.isBefore(Constants.EARLIEST_RELEASE_DATE)) {
            throw new BadRequestException("Дата выпуска должна быть не ранее 28 Декабря 1895.");
        }

        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}
