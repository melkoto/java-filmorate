package ru.yandex.practicum.storages.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    private long id = 1;

    private long getId() {
        return id++;
    }

    @Override
    public Film addFilm(Film film) {
        long id = getId();

        film.setId(id);
        films.put(id, film);

        log.info("Фильм {} добавлен в базу", film.getName());

        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Получен список всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Получение фильма с id = {}", id);
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        films.put(id, film);

        log.info("Фильм с id = {} обновлен.", id);
        return film;
    }

    @Override
    public Film deleteFilm(Long id) {
        log.info("Удаление фильма с id = {}", id);
        return films.remove(id);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение списка самых популярных фильмов.");

        return films.values()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes()
                        .size()).limit(count)
                .collect(Collectors.toList());
    }
}
