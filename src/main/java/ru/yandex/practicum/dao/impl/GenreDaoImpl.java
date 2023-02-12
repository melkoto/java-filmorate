package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.models.Genre;

import java.util.*;

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
    public Map<Long, List<Genre>> getGenresOfFilms() {
        String sql = "SELECT f.id, fg.GENRE_ID, g.name FROM FILMS as f " +
                "LEFT JOIN FILMS_GENRES as fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g on fg.GENRE_ID = g.ID " +
                "ORDER BY f.id, fg.GENRE_ID";

        Map<Long, List<Genre>> filmGenres = new HashMap<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

        while (rs.next()) {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));

            Long filmId = rs.getLong("id");

            if (filmGenres.containsKey(filmId)) {
                if (genre.getId() != 0) {
                    filmGenres.get(filmId).add(genre);
                }
            } else {
                List<Genre> list = new java.util.ArrayList<>();

                if (genre.getId() != 0) {
                    list.add(genre);
                }

                filmGenres.put(filmId, list);
            }
        }

        return filmGenres;
    }
}
