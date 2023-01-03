package ru.yandex.practicum.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(long filmId, long userId) {
        if (!filmStorage.films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (!filmStorage.films.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.films.get(filmId).setLikes(userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (!filmStorage.films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (!filmStorage.films.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        filmStorage.films.get(filmId).removeLikes(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
