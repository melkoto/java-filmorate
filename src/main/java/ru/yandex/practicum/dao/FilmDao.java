package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Film;

import java.util.List;

public interface FilmDao {
    Integer addFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Long id);

    void updateFilm(Film film);

    void deleteFilm(Long id);

    List<Film> getPopularFilms(int count);

    void likeFilm(long filmId, long userId);

    void removeLike(long filmId, long userId);

    void insertFilmGenres(long filmId, List<Integer> batchArgs);

    boolean filmDoesNotExist(long id);
}
