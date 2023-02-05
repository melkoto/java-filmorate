package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Genre;

import java.util.List;

public interface GenreDao {
    Genre getGenreById(long id);

    List<Genre> getGenres();
}
