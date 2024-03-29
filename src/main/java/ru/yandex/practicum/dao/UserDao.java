package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    List<User> getUsers();

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    String addFriend(Long userId, Long friendId);

    String removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    Boolean hasRequest(Long userId, Long friendId);

    Boolean userDoesNotExist(Long userId);

    void acceptRequest(Long userId, Long friendId);
}
