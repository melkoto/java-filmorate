package ru.yandex.practicum.storages.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    private long getId() {
        return id++;
    }

    @Override
    public User addUser(User user) {
        long id = getId();

        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }

        user.setId(id);
        users.put(id, user);

        log.info("Пользователь {} добавлен в базу", user.getName());
        return user;
    }

    public boolean doesNotHave(Long id) {
        log.info("Проверка на существование пользователя с id = {}", id);
        return !users.containsKey(id);
    }

    @Override
    public List<User> getUsers() {
        log.info("Получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        log.info("Получение пользователя с id = {}", id);
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();

        users.put(id, user);
        log.info("Пользователь с id = {} обновлен", id);

        return user;
    }

    @Override
    public User deleteUser(Long id) {
        return users.remove(id);
    }
}
