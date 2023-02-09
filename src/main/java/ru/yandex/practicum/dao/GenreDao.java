package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Genre;

public interface GenreDao {
    Genre getGenreById(int id);

    SqlRowSet getGenres();

    Boolean genreDoesNotExist(long id);

    SqlRowSet getGenreNameById(long id);

    SqlRowSet getGenresByFilmId(Long filmId);

    SqlRowSet getUniqueGenresByFilmId(Long filmId);
}
