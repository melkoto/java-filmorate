package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;
import ru.yandex.practicum.models.Mpa;

import java.sql.PreparedStatement;
import java.sql.Statement;
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

        String insertFilm = "INSERT INTO films (name, release_date, description, duration, mpa_id) "
                + "VALUES (?, ?, ?, ?, ?)";

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
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
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
                Film f = new Film();
                f.setId(rs.getInt("id"));
                f.setName(rs.getString("name"));
                f.setDescription(rs.getString("description"));
                f.setReleaseDate(rs.getDate("release_date").toLocalDate());
                f.setDuration(rs.getInt("duration"));

                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                String mpaName = getMpaById(mpa.getId()).get().getName();
                mpa.setName(mpaName);
                f.setMpa(mpa);

                Genre[] genres = genresList.toArray(new Genre[0]);
                f.setGenres(genres);

                return f;
            } else {
                throw new BadRequestException("Фильм с id " + id + " не найден");
            }
        });

        return Optional.ofNullable(film);
    }

    public Optional<Mpa> getMpaById(Long id) {
        String sql = "SELECT * FROM mpas WHERE id = ?";

        Mpa mpa = jdbcTemplate.query(sql, new Object[]{1}, (rs) -> {
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
