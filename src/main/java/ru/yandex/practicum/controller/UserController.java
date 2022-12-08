package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.AlreadyExistsException;
import ru.yandex.practicum.exceptions.DoesntExistException;
import ru.yandex.practicum.exceptions.InvalidLoginException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        int id = user.getId();

        if (user.getLogin().split(" ").length > 1) {
            log.error("Login cant have empty space");
            throw new InvalidLoginException("Login cant have empty space");
        }

        if (users.containsKey(id)) {
            log.error("User with id = {} already exists.", id);
            throw new AlreadyExistsException("User with id = " + id + " already exists.");
        }

        users.put(id, user);
        log.info("Added {} to users data", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, @RequestParam(value = "id", required = true) int id) {

        if (user.getLogin().split(" ").length > 1) {
            log.error("Login cant have empty space");
            throw new InvalidLoginException("Login cant have empty space");
        }

        if (!users.containsKey(id)) {
            log.error("User with id = {} does not exist.", user.getId());
            throw new DoesntExistException("User with id = " + id + " does not exist.");
        }

        users.put(id, user);
        log.info("User with id = {} is updated", user.getId());
        return user;
    }
}
