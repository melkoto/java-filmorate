package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (areThereDuplicates(user.getLogin(), "login") || areThereDuplicates(user.getEmail(), "email")) {
            throw new BadRequestException("Пользователь с таким логином или email уже существует");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (name, email, login, birthday) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> {

            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());

            return user;
        });
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);

        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("id"), userRows.getString("name"));

            User user = new User();
            user.setId(id);
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setBirthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());

        String sql = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ?  WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User deleteUser(Long id) {
        Optional<User> user = getUserById(id);
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return user.orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден."));
    }

    public String addFriend(Long fromUserId, Long toUserId) {
        if (getUserById(fromUserId).isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + fromUserId + " не найден.");
        }

        if (getUserById(toUserId).isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + toUserId + " не найден.");
        }

        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException("Нельзя добавить себя в друзья.");
        }

        if (hasRequest(toUserId, fromUserId)) {
            String sql = "UPDATE friends SET status = ? WHERE from_user = ? AND to_user = ?";
            jdbcTemplate.update(sql, "accepted", toUserId, fromUserId);
            return String.format("Пользователь с идентификатором %d и пользователь с идентификатором %d теперь друзья.", fromUserId, toUserId);
        }

        if (hasRequest(fromUserId, toUserId)) {
            throw new BadRequestException("Запрос на добавление в друзья уже существует.");
        }

        jdbcTemplate.update("INSERT INTO friends (from_user, to_user) VALUES (?, ?)", fromUserId, toUserId);

        return String.format("Запрос на добавление в друзья от пользователя с идентификатором %d пользователю с идентификатором %d отправлен.", fromUserId, toUserId);
    }

    @Override
    public String removeFriend(Long fromUserId, Long toUserId) {
        if (getUserById(fromUserId).isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + fromUserId + " не найден.");
        }

        if (getUserById(toUserId).isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + toUserId + " не найден.");
        }

        if (!hasRequest(fromUserId, toUserId)) {
            throw new BadRequestException("Нельзя удалить пользователя из друзей, если он не является вашим другом.");
        }

        jdbcTemplate.update("DELETE FROM friends WHERE from_user = ? AND to_user = ?", fromUserId, toUserId);
        return String.format("Пользователь с идентификатором %d удален из друзей пользователя с идентификатором %d.", toUserId, fromUserId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        if (getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден.");
        }

        String sqlFrom = "SELECT TO_USER FROM FRIENDS WHERE FROM_USER =" + userId;
        String sqlTo = "SELECT FROM_USER FROM FRIENDS WHERE TO_USER =" + userId + " AND STATUS = 'accepted'";
        List<Long> idsFrom = jdbcTemplate.queryForList(sqlFrom, Long.class);
        List<Long> idsTo = jdbcTemplate.queryForList(sqlTo, Long.class);
        List<Long> allFriends = Stream.concat(idsFrom.stream(), idsTo.stream()).distinct().collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (Long i : allFriends) {
            users.add(getUserById(i).get());
        }
        return users;
    }

    private boolean hasRequest(Long fromUserId, Long toUserId) {
        String sqlFrom = "SELECT * FROM friends WHERE from_user = ? AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, fromUserId, toUserId);

        return userRows.next();
    }

    private boolean areThereDuplicates(String field, String column) {
        String sql = "SELECT * FROM users WHERE " + column + " = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, field);
        return userRows.next();
    }
}
