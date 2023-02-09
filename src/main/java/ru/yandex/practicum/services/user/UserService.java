package ru.yandex.practicum.services.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return userDao.addUser(user);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(Long id) {
        User user = userDao.getUserById(id);

        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }

        return user;
    }

    public User updateUser(User user) {
        if (userDoesNotExist(user.getId())) {
            throw new NotFoundException("Пользователь с идентификатором " + user.getId() + " не найден.");
        }

        return userDao.updateUser(user);
    }

    public User deleteUser(long id) {
        User user = getUserById(id);
        userDao.deleteUser(id);
        return user;
    }

    public String addFriend(long fromUserId, long toUserId) {
        if (fromUserId == toUserId) {
            throw new BadRequestException("Нельзя добавить себя в друзья.");
        }

        if (userDoesNotExist(fromUserId)) {
            throw new NotFoundException("Пользователь с идентификатором " + fromUserId + " не найден.");
        }

        if (userDoesNotExist(toUserId)) {
            throw new NotFoundException("Пользователь с идентификатором " + toUserId + " не найден.");
        }

        if (hasRequest(toUserId, fromUserId)) {
            userDao.acceptRequest(toUserId, fromUserId);
            return String.format("Пользователь с идентификатором %d и пользователь с идентификатором %d теперь друзья.", fromUserId, toUserId);
        }

        if (userDao.hasRequest(fromUserId, toUserId)) {
            throw new BadRequestException("Запрос на добавление в друзья уже существует.");
        }
        return userDao.addFriend(fromUserId, toUserId);
    }

    public String removeFriend(long fromUserId, long toUserId) {
        if (userDoesNotExist(fromUserId)) {
            throw new NotFoundException("Пользователь с идентификатором " + fromUserId + " не найден.");
        }

        if (userDoesNotExist(toUserId)) {
            throw new NotFoundException("Пользователь с идентификатором " + toUserId + " не найден.");
        }

        if (!hasRequest(fromUserId, toUserId)) {
            throw new BadRequestException("Нельзя удалить пользователя из друзей, если он не является вашим другом.");
        }

        return userDao.removeFriend(fromUserId, toUserId);
    }

    public List<User> getFriends(long userId) {
        if (userDoesNotExist(userId)) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден.");
        }

        List<User> friends = new ArrayList<>();
        SqlRowSet friendRows = userDao.getFriends(userId);

        while (friendRows.next()) {
            User friend = new User();
            friend.setId(friendRows.getLong("id"));
            friend.setName(friendRows.getString("name"));
            friend.setEmail(friendRows.getString("email"));
            friend.setLogin(friendRows.getString("login"));
            friend.setBirthday(Objects.requireNonNull(friendRows.getDate("birthday")).toLocalDate());
            friends.add(friend);
        }

        return friends;
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        return getFriends(userId).stream()
                .filter(user -> getFriends(anotherUserId).contains(user))
                .collect(Collectors.toList());
    }

    private Boolean hasRequest(long fromUserId, long toUserId) {
        return userDao.hasRequest(fromUserId, toUserId);
    }

    private Boolean userDoesNotExist(long userId) {
        return userDao.userDoesNotExist(userId);
    }
}
