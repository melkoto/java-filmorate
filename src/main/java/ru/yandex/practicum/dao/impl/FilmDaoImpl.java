package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.models.Film;

import java.util.List;
import java.util.Optional;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO film (name, description, releaseDate, genre, mpa, duration) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getGenre(), film.getMpa(), film.getDuration());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.empty();
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Long id) {
        return null;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }
}
