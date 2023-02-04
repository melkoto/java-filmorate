package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User addUser(User user);

    List<User> getUsers();

    Optional<User> getUserById(Long id);

    User updateUser(User user);

    User deleteUser(Long id);

    String addFriend(Long userId, Long friendId);

    String removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);
}
