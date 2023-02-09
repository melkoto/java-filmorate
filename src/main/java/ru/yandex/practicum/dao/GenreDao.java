package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getGenreById(int id);

    List<Genre> getGenres();

    Boolean genreDoesNotExist(long id);

    String getGenreNameById(long id);

    Set<Genre> getGenresByFilmId(Long filmId);

    SqlRowSet getUniqueGenresByFilmId(Long filmId);
}
