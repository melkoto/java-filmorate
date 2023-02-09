package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?";

        SqlRowSet genre = jdbcTemplate.queryForRowSet(sql, id);

        if (genre.next()) {
            Genre g = new Genre();
            g.setId(genre.getInt("id"));
            g.setName(genre.getString("name"));
            return g;
        }

        return null;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Genre genre = new Genre();
            genre.setId(resultSet.getInt("id"));
            genre.setName(resultSet.getString("name"));
            return genre;
        });
    }

    @Override
    public Boolean genreDoesNotExist(long id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id =?", new Object[]{id}).next();
    }

    @Override
    public String getGenreNameById(long id) {
        String sql = "SELECT name FROM genres WHERE id = ?";

        SqlRowSet genre = jdbcTemplate.queryForRowSet(sql, id);

        if (genre.next()) {
            return genre.getString("name");
        }

        return null;
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long filmId) {
        String sql = "SELECT fg.GENRE_ID, g.name FROM FILMS_GENRES as fg " +
                "LEFT JOIN GENRES g on fg.GENRE_ID = g.ID " +
                "WHERE FILM_ID = ? ORDER BY fg.GENRE_ID";

        Set<Genre> genres = new HashSet<>();
        SqlRowSet genre = jdbcTemplate.queryForRowSet(sql, filmId);

        while (genre.next()) {
            Genre g = new Genre();
            g.setId(genre.getInt("genre_id"));
            g.setName(genre.getString("name"));
            genres.add(g);
        }

        return genres;
    }

    @Override
    public SqlRowSet getUniqueGenresByFilmId(Long filmId) {
        return jdbcTemplate.queryForRowSet("SELECT DISTINCT fg.GENRE_ID, g.name FROM FILMS_GENRES as fg " +
                "LEFT JOIN GENRES g on fg.GENRE_ID = g.ID " +
                "WHERE FILM_ID = ? ORDER BY fg.GENRE_ID", filmId);
    }
}
