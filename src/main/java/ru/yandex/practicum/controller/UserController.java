package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        int id = getId();

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getLogin().split(" ").length > 1) {
            throw new ValidationException("Login cant have empty space");
        }

        if (users.containsKey(id)) {
            throw new BadRequestException("User with id = " + id + " already exists.");
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
            throw new ValidationException("Login cant have empty space");
        }

        if (!users.containsKey(id)) {
            log.error("User with id = {} does not exist.", id);
            throw new BadRequestException("User with id = " + id + " does not exist.");

        }

        users.put(id, user);
        log.info("User with id = {} is updated", id);
        return user;
    }

    private int getId() {
        return id++;
    }
}
