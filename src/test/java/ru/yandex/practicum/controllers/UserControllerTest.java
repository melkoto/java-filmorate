package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.services.user.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserService userService;

    @Autowired

    @BeforeEach
    void setUp() {
    }

    @Test
    void getUsers() {
        assertTrue(true);
    }

    @Test
    void createUser() {
        assertTrue(true);
    }

    @Test
    void updateUser() {
        assertTrue(true);
    }
}
