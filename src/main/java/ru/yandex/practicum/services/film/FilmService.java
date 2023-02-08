package ru.yandex.practicum.services.film;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Film;
import ru.yandex.practicum.models.Genre;
import ru.yandex.practicum.services.genre.GenreService;
import ru.yandex.practicum.services.mpa.MpaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRowSet = filmDao.getFilms();

        while (sqlRowSet.next()) {
            Film film = buildFilm(sqlRowSet);

            films.add(film);
        }
        // films.sort(Comparator.comparing(Film::getId));
        return films;
    }

    public Film getFilmById(Long id) {
        SqlRowSet sqlRowSet = filmDao.getFilmById(id);

        if (!sqlRowSet.next()) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        return buildFilm(sqlRowSet);
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

    public List<Film> getPopularFilms(int count) {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRowSet = filmDao.getPopularFilms(count);

        while (sqlRowSet.next()) {
            Film film = buildFilm(sqlRowSet);

            films.add(film);
        }
        return films;
    }

    @NotNull
    private Film buildFilm(SqlRowSet sqlRowSet) {
        Film film = new Film();
        film.setId(sqlRowSet.getLong("id"));
        film.setName(sqlRowSet.getString("name"));
        film.setDescription(sqlRowSet.getString("description"));
        film.setReleaseDate(Objects.requireNonNull(sqlRowSet.getDate("release_date")).toLocalDate());
        film.setDuration(sqlRowSet.getInt("duration"));

        film.setMpa(mpaService.getMpaById(sqlRowSet.getInt("mpa_id")));
        Set<Genre> currGenres = genreService.getGenresByFilmId(sqlRowSet.getLong("id"));

        Genre[] allGenres = new Genre[currGenres.size()];
        film.setGenres(currGenres.toArray(allGenres));
        return film;
    }

    public boolean filmDoesNotExist(Long id) {
        return filmDao.filmDoesNotExist(id);
    }
}
