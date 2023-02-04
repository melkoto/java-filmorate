package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.services.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getLogin().split(" ").length > 1) {
            throw new BadRequestException("Логин должен быть без пробела");
        }

        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (user.getLogin().split(" ").length > 1) {
            log.error("Логин должен быть без пробелов");
            throw new BadRequestException("Логин должен быть без пробелов");
        }

        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}/{status}")
    public void acceptFriend(@PathVariable long id, @PathVariable long friendId, @PathVariable String status) {
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public User removeUser(@PathVariable long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
