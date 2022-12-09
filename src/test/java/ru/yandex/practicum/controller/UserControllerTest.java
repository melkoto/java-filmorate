package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;

class UserControllerTest {
    UserController userController;
    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user1 = new User();
        user1.setId(1);
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.of(1989, Month.AUGUST, 31));

        user2 = new User();
        user2.setId(1);
        user2.setEmail("test-another@test.com");
        user2.setLogin("test-another");
        user2.setName("Another");
        user2.setBirthday(LocalDate.of(1994, Month.JANUARY, 29));
    }

    @Test
    void getUsers() {
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser() {
        userController.createUser(user1);
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUser() {
        userController.createUser(user1);
        userController.updateUser(user2);
        Assertions.assertEquals(userController.getUsers().get(1), user2);
    }
}
