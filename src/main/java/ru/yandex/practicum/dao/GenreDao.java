package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenreDao {
    Genre getGenreById(int id);

    List<Genre> getGenres();

    Set<Genre> getGenresByFilmId(Long filmId);

    Map<Long, List<Genre>> getGenresOfFilms();
}
