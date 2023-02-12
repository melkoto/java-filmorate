package ru.yandex.practicum.services.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;
import ru.yandex.practicum.models.Mpa;
import ru.yandex.practicum.services.genre.GenreService;
import ru.yandex.practicum.services.mpa.MpaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmDao filmDao;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Autowired
    public FilmService(FilmDao filmDao, GenreService genreService, MpaService mpaService) {
        this.filmDao = filmDao;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public Film addFilm(Film film) {
        List<Genre> genres = genreService.getGenres();
        List<Integer> genreIds = genreService.getGenres().stream().map(Genre::getId).collect(Collectors.toList());

        long filmId = filmDao.addFilm(film);

        film.setId(filmId);
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));

        List<Integer> batchArgs = new ArrayList<>();

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!genreIds.contains(genre.getId())) {
                    throw new BadRequestException("Жанр с id " + genre.getId() + " не найден");
                }
                String name = genres.stream().filter(g -> g.getId() == genre.getId()).findFirst().get().getName();
                genre.setName(name);
                batchArgs.add(genre.getId());
            }
        } else {
            film.setGenres(new Genre[0]);
        }

        filmDao.insertFilmGenres(filmId, batchArgs);

        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDao.getFilms();
        updateFilmData(films);
        return films;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmDao.getPopularFilms(count);
        updateFilmData(films);
        return films;
    }

    public Film getFilmById(Long id) {
        Film film = filmDao.getFilmById(id);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        film.setGenres(genreService.getUniqueGenresByFilmId(id).toArray(new Genre[0]));

        return film;
    }

    public Film updateFilm(Film film) {
        if (filmDoesNotExist(film.getId())) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        filmDao.updateFilm(film);

        film.setGenres(genreService.getUniqueGenresByFilmId(film.getId()).toArray(new Genre[0]));
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));

        return film;
    }

    public void deleteFilm(Long id) {
        if (filmDoesNotExist(id)) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        filmDao.deleteFilm(id);
    }

    public void likeFilm(long filmId, long userId) {
        if (filmDoesNotExist(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        if (filmDoesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        filmDao.likeFilm(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        if (filmDoesNotExist(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        if (filmDoesNotExist(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        filmDao.removeLike(filmId, userId);
    }

    public boolean filmDoesNotExist(Long id) {
        return filmDao.filmDoesNotExist(id);
    }

    private void updateFilmData(List<Film> films) {
        Map<Long, List<Genre>> genresOfFilms = genreService.getGenresOfFilms();
        List<Mpa> mpas = mpaService.getAllMpas();

        for (Film film : films) {
            film.setMpa(mpas.stream()
                    .filter(mpa -> mpa.getId() == film.getMpa().getId())
                    .findFirst()
                    .get());

            if (genresOfFilms.get(film.getId()).isEmpty()) {
                film.setGenres(new Genre[0]);
                continue;
            }

            film.setGenres(genresOfFilms.get(film.getId()).toArray(new Genre[0]));
        }
    }
}
