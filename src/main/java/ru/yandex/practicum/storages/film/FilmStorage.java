package ru.yandex.practicum.storages.film;

import ru.yandex.practicum.models.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Long id);

    Film updateFilm(Film film);

    Film deleteFilm(Long id);

    boolean doesNotHave(Long id);

    List<Film> getPopularFilms(int count);
}
