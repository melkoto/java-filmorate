package ru.yandex.practicum.storages.user;

import ru.yandex.practicum.models.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    List<User> getUsers();

    User getUserById(Long id);

    User updateUser(User user);

    User deleteUser(Long id);

    boolean doesNotHave(Long id);
}
