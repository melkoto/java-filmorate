package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film addFilm(Film film);

    List<Film> getFilms();

    Optional<Film> getFilmById(Long id);

    Film updateFilm(Film film);

    Film deleteFilm(Long id);

    List<Film> getPopularFilms(int count);

    Optional<Mpa> getMpaById(Long id);
}
