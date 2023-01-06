package ru.yandex.practicum.services.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.storages.film.FilmStorage;
import ru.yandex.practicum.storages.film.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(long filmId, long userId) {
        if (filmStorage.doesNotExist(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (filmStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.getFilmById(filmId).setLikes(userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (filmStorage.doesNotExist(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (filmStorage.doesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.getFilmById(filmId).removeLikes(userId);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        if (filmStorage.doesNotExist(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        long id = film.getId();

        if (filmStorage.doesNotExist(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
