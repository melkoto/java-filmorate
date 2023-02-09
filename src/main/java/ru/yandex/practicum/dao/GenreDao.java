package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Genre;

import java.util.List;

public interface GenreDao {
    Genre getGenreById(int id);

    List<Genre> getGenres();

    Boolean genreDoesNotExist(long id);

    String getGenreNameById(long id);

    SqlRowSet getGenresByFilmId(Long filmId);

    SqlRowSet getUniqueGenresByFilmId(Long filmId);
}
