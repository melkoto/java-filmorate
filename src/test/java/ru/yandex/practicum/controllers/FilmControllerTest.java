package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.services.user.UserService;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getFilms() {
    }

    @Test
    void addFilmShouldAddWithUniqueId() {
    }

    @Test
    void updateFilm() {
    }
}
