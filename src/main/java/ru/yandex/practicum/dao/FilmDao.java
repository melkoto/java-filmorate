package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Film;

import java.util.List;

public interface FilmDao {
    Film addFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Long id);

    Film updateFilm(Film film);

    Film deleteFilm(Long id);

    List<Film> getPopularFilms(int count);
}
