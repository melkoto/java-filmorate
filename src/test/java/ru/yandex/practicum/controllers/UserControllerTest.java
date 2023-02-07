package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.services.user.UserService;

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
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }
}
