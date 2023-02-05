package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;
import ru.yandex.practicum.models.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM MPAS WHERE id =?", new Object[]{film.getMpa().getId()}).next()) {
            throw new BadRequestException("Возрастной рейтинг с id " + film.getMpa().getId() + " не найден");
        }

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

        long filmId = Objects.requireNonNull(filmKeyHolder.getKey()).longValue();
        film.setId(filmId);
        String mpaName = jdbcTemplate.queryForObject("SELECT name FROM MPAS WHERE id =?", new Object[]{film.getMpa().getId()}, String.class);

        film.getMpa().setName(mpaName);

        if (film.getGenres() != null) {
            String insertFilmGenre = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {

                SqlRowSet genreId = jdbcTemplate.queryForRowSet("SELECT id FROM genres WHERE id = ?", genre.getId());
                if (!genreId.next()) {
                    throw new BadRequestException("Жанр с id " + genre.getId() + " не найден");
                }

                String name = jdbcTemplate.queryForObject("SELECT name FROM genres WHERE id = ?", String.class, genre.getId());
                genre.setName(name);
                jdbcTemplate.update(insertFilmGenre, filmId, genre.getId());
            }
        }

        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> result = new ArrayList<>();
        String sql = "SELECT id FROM films";
        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class);
        for (Long id : ids) {
            result.add(getFilmById(id).get());
        }
        return result;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id =?", new Object[]{id}).next()) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        String filmSql = "SELECT * FROM films WHERE id = ?";
        String genresSql = "SELECT g.* FROM genres g JOIN films_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";

        List<Genre> genresList = jdbcTemplate.query(genresSql, new Object[]{id}, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        });

        Film film = jdbcTemplate.query(filmSql, new Object[]{id}, (rs) -> {
            if (rs.next()) {
                return getFilmGenre(genresList, rs);
            } else {
                throw new BadRequestException("Фильм с id " + id + " не найден");
            }
        });

        return Optional.ofNullable(film);
    }

    public Optional<Mpa> getMpaById(Long id) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM MPAS WHERE id =?", new Object[]{id}).next()) {
            throw new BadRequestException("Возрастной рейтинг с id " + id + " не найден");
        }

        String sql = "SELECT * FROM mpas WHERE id = ?";

        Mpa mpa = jdbcTemplate.query(sql, new Object[]{id}, (rs) -> {
            if (rs.next()) {
                Mpa m = new Mpa();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                return m;
            } else {
                throw new BadRequestException("Возрастной рейтинг с id " + id + " не найден");
            }
        });

        return Optional.ofNullable(mpa);
    }

    @Override
    public void likeFilm(long filmId, long userId) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id =?", new Object[]{filmId}).next()) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        if (!jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id =?", new Object[]{userId}).next()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id =?", new Object[]{filmId}).next()) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        if (!jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id =?", new Object[]{userId}).next()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id =?", new Object[]{film.getId()}).next()) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        List<Long> genresList = new ArrayList<>();
        List<Genre> uniqueGenres = new ArrayList<>();

        String sql = "UPDATE films SET name = ?, release_date = ?, description = ?, duration = ?, mpa_id = ? WHERE id = ?";
        String deleteGenres = "DELETE FROM films_genres WHERE film_id = ?";
        String insertGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(), film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(deleteGenres, film.getId());

        String mpaName = jdbcTemplate.queryForObject("SELECT name FROM MPAS WHERE id =?", new Object[]{film.getMpa().getId()}, String.class);
        film.getMpa().setName(mpaName);

        if (film.getGenres() == null) {
            return film;
        }

        for (Genre genre : film.getGenres()) {
            if (!genresList.contains(genre.getId())) {
                String genreName = jdbcTemplate.queryForObject("SELECT name FROM genres WHERE id =?", new Object[]{genre.getId()}, String.class);
                genresList.add(genre.getId());
                jdbcTemplate.update(insertGenres, film.getId(), genre.getId());
                genre.setName(genreName);

                uniqueGenres.add(genre);
            }
        }

        film.setGenres(uniqueGenres.toArray(new Genre[0]));

        return film;
    }

    @Override
    public Film deleteFilm(Long id) {
        Film film = getFilmById(id).orElseThrow(() -> new BadRequestException("Фильм с id " + id + " не найден"));

        String sql = "DELETE FROM films WHERE id = ?";
        String deleteGenres = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, id);
        jdbcTemplate.update(sql, id);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT films.id, films.name, films.description, films.release_date, films.duration, films.mpa_id, COUNT(likes.film_id) AS likes_count FROM films LEFT JOIN likes ON films.id = likes.film_id GROUP BY films.id ORDER BY likes_count DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, new Object[]{count}, (rs) -> {
            List<Film> filmsList = new ArrayList<>();
            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));

                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                String mpaName = getMpaById(mpa.getId()).get().getName();
                mpa.setName(mpaName);
                film.setMpa(mpa);

                filmsList.add(film);
            }
            return filmsList;
        });

        return films;
    }

    @NotNull
    private Film getFilmGenre(List<Genre> genresList, ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        String mpaName = getMpaById(mpa.getId()).get().getName();
        mpa.setName(mpaName);
        film.setMpa(mpa);

        Genre[] genres = genresList.toArray(new Genre[0]);
        film.setGenres(genres);

        return film;
    }
}
