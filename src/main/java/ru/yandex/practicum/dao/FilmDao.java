package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Integer addFilm(Film film);

    SqlRowSet getFilms();

    SqlRowSet getFilmById(Long id);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    List<Film> getPopularFilms(int count);

    Optional<Mpa> getMpaById(int id);

    void likeFilm(long filmId, long userId);

    void removeLike(long filmId, long userId);

    void insertFilmGenres(long filmId, List<Integer> batchArgs);
}
