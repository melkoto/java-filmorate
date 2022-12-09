package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;
    private Film film1;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setId(1);
        film.setName("Naruto");
        film.setDescription("Anime");
        film.setReleaseDate(LocalDate.of(2000, Month.SEPTEMBER, 23));
        film.setDuration(6000);

        film1 = new Film();
        film1.setId(1);
        film1.setName("Death Note");
        film1.setDescription("Anime");
        film1.setReleaseDate(LocalDate.of(2010, Month.JULY, 11));
        film1.setDuration(1200);
    }

    @Test
    void getFilms() {
        Assertions.assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void addFilmShouldAddWithUniqueId() {
        filmController.addFilm(film);
        Assertions.assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void updateFilm() {
        filmController.addFilm(film);
        filmController.updateFilm(film1);
        Assertions.assertEquals(filmController.getFilms().get(0), film1);
    }
}
