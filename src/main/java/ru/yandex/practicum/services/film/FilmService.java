package ru.yandex.practicum.services.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.models.Film;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmDao filmDao;

    public FilmService(FilmDao filmDao) {
        this.filmDao = filmDao;
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
        return filmDao.updateFilm(film);
    }

    public Film deleteFilm(Long id) {
        return filmDao.deleteFilm(id);
    }

    public void likeFilm(long filmId, long userId) {
        filmDao.likeFilm(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmDao.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmDao.getPopularFilms(count);
    }
}
