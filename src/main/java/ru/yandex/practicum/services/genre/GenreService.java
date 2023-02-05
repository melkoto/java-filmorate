package ru.yandex.practicum.services.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.models.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(long id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }
}
