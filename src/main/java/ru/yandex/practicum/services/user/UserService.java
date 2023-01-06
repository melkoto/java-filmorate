package ru.yandex.practicum.services.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.storages.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(long userId, long friendId) {
        if (userStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (userStorage.doesNotExist(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }

        userStorage.getUserById(userId).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if (userStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (userStorage.doesNotExist(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }

        userStorage.getUserById(userId).removeFriend(friendId);
    }

    public List<User> getFriends(long userId) {
        if (userStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        return getUsersByIds(userStorage.getUserById(userId).getFriends());
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        if (userStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (userStorage.doesNotExist(anotherUserId)) {
            throw new NotFoundException("Пользователь с id = " + anotherUserId + " не найден");
        }
        List<Long> commonFriends = new ArrayList<>();
        Set<Long> friends = new HashSet<>();

        List<Long> common = Stream.concat(
                userStorage.getUserById(userId).getFriends().stream(),
                userStorage.getUserById(anotherUserId).getFriends().stream()
        ).collect(Collectors.toList());

        for (Long friendId : common) {
            if (!friends.add(friendId)) {
                commonFriends.add(friendId);
            } else {
                friends.add(friendId);
            }
        }

        return getUsersByIds(commonFriends);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    private List<User> getUsersByIds(List<Long> ids) {
        return ids.stream().map(userStorage::getUserById).collect(Collectors.toList());
    }
}
