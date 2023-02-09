package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addFilm(Film film) {
        String insertFilm = "INSERT INTO films (name, release_date, description, duration, mpa_id) " + "VALUES (?, ?, ?, ?, ?)";

        KeyHolder filmKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertFilm, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, filmKeyHolder);

        return Objects.requireNonNull(filmKeyHolder.getKey()).intValue();
    }

    @Override
    public SqlRowSet getFilms() {
        return jdbcTemplate.queryForRowSet("SELECT * FROM films ORDER BY id");
    }

    @Override
    public SqlRowSet getFilmById(Long id) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", id);
    }

    @Override
    public void likeFilm(long filmId, long userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    @Override
    public void updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, release_date = ?, description = ?, duration = ?, mpa_id = ? WHERE id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(), film.getDuration(), film.getMpa().getId(), film.getId());

        if (film.getGenres() == null) {
            return;
        }

        updateFilmGenres(film);
    }

    @Override
    public void deleteFilm(Long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", id);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT films.id, films.name, films.description, films.release_date, films.duration, films.mpa_id, COUNT(likes.film_id) AS likes_count " +
                "FROM films LEFT JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, new Object[]{count}, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            return film;
        });
    }

    @Override
    public boolean filmDoesNotExist(long id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", new Object[]{id}).next();
    }

    public void insertFilmGenres(long filmId, List<Integer> genres) {
        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setInt(2, genres.get(i));
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    private void updateFilmGenres(Film film) {
        String deleteGenres = "DELETE FROM films_genres WHERE film_id = ?";
        String insertGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.update(deleteGenres, film.getId());

        if (film.getGenres() == null) {
            return;
        }

        List<Integer> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            if (!genres.contains(genre.getId())) {
                genres.add(genre.getId());
            }
        }

        insertFilmGenres(film.getId(), genres);
    }
}
