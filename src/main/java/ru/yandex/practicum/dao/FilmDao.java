package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Film;

import java.util.List;

public interface FilmDao {
    Integer addFilm(Film film);

    SqlRowSet getFilms();

    SqlRowSet getFilmById(Long id);

    void updateFilm(Film film);

    void deleteFilm(Long id);

    List<Film> getPopularFilms(int count);

    void likeFilm(long filmId, long userId);

    void removeLike(long filmId, long userId);

    void insertFilmGenres(long filmId, List<Integer> batchArgs);

    boolean filmDoesNotExist(long id);
}
