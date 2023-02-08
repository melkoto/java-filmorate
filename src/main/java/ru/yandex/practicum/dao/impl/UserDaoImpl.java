package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.models.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addUser(User user) {
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

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
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
    public SqlRowSet getUserById(Long id) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ?  WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public String addFriend(Long fromUserId, Long toUserId) {
        jdbcTemplate.update("INSERT INTO friends (from_user, to_user) VALUES (?, ?)", fromUserId, toUserId);
        return String.format("Запрос на добавление в друзья от пользователя с идентификатором %d пользователю с идентификатором %d отправлен.", fromUserId, toUserId);
    }

    @Override
    public String removeFriend(Long fromUserId, Long toUserId) {
        jdbcTemplate.update("DELETE FROM friends WHERE from_user = ? AND to_user = ?", fromUserId, toUserId);
        return String.format("Пользователь с идентификатором %d удален из друзей пользователя с идентификатором %d.", toUserId, fromUserId);
    }

    @Override
    public SqlRowSet getFriends(Long id) {
        String sql = "SELECT * FROM users RIGHT JOIN (" +
                "(SELECT TO_USER AS users FROM FRIENDS WHERE FROM_USER = ?) UNION (" +
                "SELECT FROM_USER FROM FRIENDS WHERE TO_USER= ? AND status='accepted')) ON users.id = users";
        return jdbcTemplate.queryForRowSet(sql, id, id);
    }

    @Override
    public Boolean hasRequest(Long fromUserId, Long toUserId) {
        String sqlFrom = "SELECT * FROM friends WHERE from_user = ? AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, fromUserId, toUserId);

        return userRows.next();
    }

    @Override
    public Boolean userDoesNotExist(Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);
        return !userRows.next();
    }

    @Override
    public void acceptRequest(Long fromUserId, Long toUserId) {
        String sql = "UPDATE friends SET status = ? WHERE from_user = ? AND to_user = ?";
        jdbcTemplate.update(sql, "accepted", toUserId, fromUserId);
    }
}
