package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface GenreDao {
    SqlRowSet getGenreById(int id);

    SqlRowSet getGenres();

    Boolean genreDoesNotExist(long id);

    SqlRowSet getGenreNameById(long id);

    SqlRowSet getGenresByFilmId(Long filmId);
}
