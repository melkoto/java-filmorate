package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SqlRowSet getGenreById(int id) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", id);
    }

    @Override
    public SqlRowSet getGenres() {
        return jdbcTemplate.queryForRowSet("SELECT * FROM genres ORDER BY id");
    }

    @Override
    public Boolean genreDoesNotExist(long id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id =?", new Object[]{id}).next();
    }

    @Override
    public SqlRowSet getGenreNameById(long id) {
        return jdbcTemplate.queryForRowSet("SELECT name FROM genres WHERE id = ?", id);
    }

    @Override
    public SqlRowSet getGenresByFilmId(Long filmId) {
        return jdbcTemplate.queryForRowSet("SELECT fg.GENRE_ID, g.name FROM FILMS_GENRES as fg " +
                "LEFT JOIN GENRES g on fg.GENRE_ID = g.ID " +
                "WHERE FILM_ID = ? ORDER BY fg.GENRE_ID", filmId);

    }

    @Override
    public SqlRowSet getUniqueGenresByFilmId(Long filmId) {
        return jdbcTemplate.queryForRowSet("SELECT DISTINCT fg.GENRE_ID, g.name FROM FILMS_GENRES as fg " +
                "LEFT JOIN GENRES g on fg.GENRE_ID = g.ID " +
                "WHERE FILM_ID = ? ORDER BY fg.GENRE_ID", filmId);
    }
}
