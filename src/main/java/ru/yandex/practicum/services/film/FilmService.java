package ru.yandex.practicum.services.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.storages.film.FilmStorage;
import ru.yandex.practicum.storages.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmDao filmDao;

    public FilmService(InMemoryFilmStorage filmStorage, FilmDao filmDao) {
        this.filmStorage = filmStorage;
        this.filmDao = filmDao;
    }

    public void likeFilm(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.getFilmById(filmId).setLikes(userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.getFilmById(filmId).removeLikes(userId);
    }

    public Film addFilm(Film film) {
        return filmDao.addFilm(film);
    }

    public List<Film> getFilms() {
        return filmDao.getFilms();
    }

    public Optional<Film> getFilmById(Long id) {
        return filmDao.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        long id = film.getId();

        if (filmStorage.getFilmById(id) == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
