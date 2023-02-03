package ru.yandex.practicum.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class User {
    private final Set<Long> friends = new HashSet<>();

    @NotNull
    private long id;

    @Email(message = "Введите валидную почту")
    private String email;

    private String name;

    @NotEmpty
    @NotNull
    private String login;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Введите год рождения")
    private LocalDate birthday;

    public void addFriend(long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
    }

    public List<Long> getFriends() {
        return new ArrayList<>(friends);
    }
}
