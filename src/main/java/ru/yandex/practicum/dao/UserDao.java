package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    List<User> getUsers();

    User getUserById(Long id);

    User updateUser(User user);

    User deleteUser(Long id);
}
