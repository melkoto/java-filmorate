package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.AlreadyExistsException;
import ru.yandex.practicum.exceptions.DoesntExistException;
import ru.yandex.practicum.exceptions.InvalidLoginException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.utils.IdCreator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        int id = IdCreator.getId();

        if (user.getLogin().split(" ").length > 1) {
            // Зачем логировать, если есть exception?
            // log.error("Login cant have empty space");
            throw new InvalidLoginException("Login cant have empty space");
        }

        if (users.containsKey(id)) {
            // log.error("User with id = {} already exists.", id);
            throw new AlreadyExistsException("User with id = " + id + " already exists.");
        }

        user.setId(id);
        users.put(id, user);
        log.info("Added {} to users data", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        int id = user.getId();

        if (user.getLogin().split(" ").length > 1) {
            log.error("Login cant have empty space");
            throw new InvalidLoginException("Login cant have empty space");
        }

        if (!users.containsKey(id)) {
            log.error("User with id = {} does not exist.", id);
            throw new DoesntExistException("User with id = " + id + " does not exist.");
        }

        users.put(id, user);
        log.info("User with id = {} is updated", id);
        return user;
    }
}
