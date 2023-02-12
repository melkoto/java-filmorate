package ru.yandex.practicum.services.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(int id) {
        Genre genre = genreDao.getGenreById(id);

        if (genre == null) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }

        return genre;
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Map<Long, List<Genre>> getGenresOfFilms() {
        return genreDao.getGenresOfFilms();
    }


    public Set<Genre> getGenresByFilmId(Long filmId) {
        return genreDao.getGenresByFilmId(filmId);
    }

    public List<Genre> getUniqueGenresByFilmId(Long filmId) {
        Set<Genre> genres = getGenresByFilmId(filmId);
        return new ArrayList<>(genres);
    }
}
