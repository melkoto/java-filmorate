package ru.yandex.practicum.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.services.user.UserService;
import ru.yandex.practicum.storages.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserService userService;

    @Autowired
    InMemoryUserStorage userStorage;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
//        user1.setId(1);
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.of(1989, Month.AUGUST, 31));

        user2 = new User();
//        user2.setId(1);
        user2.setEmail("test@test.com");
        user2.setLogin("test");
        user2.setName("Test");
        user2.setBirthday(LocalDate.of(1989, Month.AUGUST, 31));
    }

    @Test
    void getUsers() {
        Assertions.assertEquals(3, userStorage.getUsers().size());
    }

    @Test
    void createUser() {
        userStorage.addUser(user1);
        Assertions.assertEquals(3, userStorage.getUsers().size());
    }

    @Test
    void updateUser() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        Assertions.assertEquals(2, userStorage.getUsers().size());
    }
}
