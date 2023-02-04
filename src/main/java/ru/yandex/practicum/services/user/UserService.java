package ru.yandex.practicum.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public String addFriend(long userId, long friendId) {
        return userDao.addFriend(userId, friendId);
    }

    public String removeFriend(long userId, long friendId) {
        return userDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        return userDao.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        return userDao.getFriends(userId).stream()
                .filter(user -> userDao.getFriends(anotherUserId).contains(user))
                .collect(Collectors.toList());
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

    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    public User deleteUser(long id) {
        return userDao.deleteUser(id);
    }
}
