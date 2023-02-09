package ru.yandex.practicum.services.genre;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public Boolean genreDoesNotExist(long id) {
        return genreDao.genreDoesNotExist(id);
    }

    public String getGenreNameById(long id) {
        String name = genreDao.getGenreNameById(id);

        if (name == null) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }

        return name;
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        SqlRowSet genres = genreDao.getGenresByFilmId(filmId);
        Set<Genre> genresList = new HashSet<>();

        while (genres.next()) {
            Genre g = new Genre();
            g.setId(genres.getInt("genre_id"));
            g.setName(genres.getString("name"));
            genresList.add(g);
        }
        return genresList;
    }

    public List<Genre> getUniqueGenresByFilmId(Long filmId) {
        SqlRowSet genres = genreDao.getGenresByFilmId(filmId);
        List<Genre> genresList = new ArrayList<>();

        while (genres.next()) {
            Genre g = new Genre();
            g.setId(genres.getInt("genre_id"));
            g.setName(genres.getString("name"));
            genresList.add(g);
        }
        return genresList;
    }
}
