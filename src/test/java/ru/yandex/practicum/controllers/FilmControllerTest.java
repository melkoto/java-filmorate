package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.services.user.UserService;
import ru.yandex.practicum.storages.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    UserService userService;

    @Autowired
    InMemoryFilmStorage filmStorage;

    Film film1;
    Film film2;


    @BeforeEach
    void setUp() {
        film1 = new Film();
        film1.setId(1);
        film1.setName("Naruto");
        film1.setDescription("Anime");
        film1.setReleaseDate(LocalDate.of(2000, Month.SEPTEMBER, 23));
        film1.setDuration(6000);

        film2 = new Film();
        film2.setId(1);
        film2.setName("Death Note");
        film2.setDescription("Anime");
        film2.setReleaseDate(LocalDate.of(2010, Month.JULY, 11));
        film2.setDuration(1200);
    }

    @Test
    void getFilms() {
        Assertions.assertEquals(3, filmStorage.getFilms().size());
    }

    @Test
    void addFilmShouldAddWithUniqueId() {
        filmStorage.addFilm(film1);
        Assertions.assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void updateFilm() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        Assertions.assertEquals(3, filmStorage.getFilms().size());
    }
}
