package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long id) {
        if (!jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id =?", new Object[]{id}).next()) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }

        String sql = "SELECT * FROM genres WHERE id = ?";
        SqlRowSet genre = jdbcTemplate.queryForRowSet(sql, id);

        if (genre.next()) {
            Genre g = new Genre();
            g.setId(genre.getInt("id"));
            g.setName(genre.getString("name"));
            return g;
        } else {
            throw new BadRequestException("Жанр с id " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> result = new ArrayList<>();
        String sql = "SELECT id FROM genres ORDER BY id";
        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class);
        for (Long id : ids) {
            result.add(getGenreById(id));
        }
        return result;
    }
}
