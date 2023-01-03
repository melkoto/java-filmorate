package ru.yandex.practicum.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(long userId, long friendId) {
        if (!userStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (!userStorage.users.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }

        userStorage.users.get(userId).addFriend(friendId);
        userStorage.users.get(friendId).addFriend(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if (!userStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (!userStorage.users.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }

        userStorage.users.get(userId).removeFriend(friendId);
    }

    public List<User> getFriends(long userId) {
        if (!userStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        return getUsersByIds(userStorage.users.get(userId).getFriends());
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        if (!userStorage.users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (!userStorage.users.containsKey(anotherUserId)) {
            throw new NotFoundException("Пользователь с id = " + anotherUserId + " не найден");
        }
        List<Long> commonFriends = new ArrayList<>();
        Set<Long> friends = new HashSet<>();

        List<Long> common = Stream.concat(
                userStorage.users.get(userId).getFriends().stream(),
                userStorage.users.get(anotherUserId).getFriends().stream()
        ).toList();

        for (Long friendId : common) {
            if (!friends.add(friendId)) {
                commonFriends.add(friendId);
            } else {
                friends.add(friendId);
            }
        }

        return getUsersByIds(commonFriends);
    }

    private List<User> getUsersByIds(List<Long> ids) {
        return ids.stream().map(userStorage.users::get).toList();
    }
}
