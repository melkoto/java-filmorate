package ru.yandex.practicum.services.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;
import ru.yandex.practicum.models.Mpa;
import ru.yandex.practicum.services.genre.GenreService;
import ru.yandex.practicum.services.mpa.MpaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        }

        filmDao.insertFilmGenres(filmId, batchArgs);

        return film;
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

    private List<Mpa> getMpas() {
        return mpaService.getAllMpas();
    }
}
