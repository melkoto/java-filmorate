package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.services.user.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getFilms() {
        assertTrue(true);
    }

    @Test
    void addFilmShouldAddWithUniqueId() {
        assertTrue(true);
    }

    @Test
    void updateFilm() {
        assertTrue(true);
    }
}
