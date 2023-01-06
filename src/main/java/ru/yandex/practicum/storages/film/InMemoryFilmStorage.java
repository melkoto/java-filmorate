package ru.yandex.practicum.storages.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.NotFoundException;
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

        log.info("Фильм с id = {} добавлен в базу.", id);

        return film;
    }

    public boolean doesNotExist(Long id) {
        return !films.containsKey(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {

        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();

        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        films.put(id, film);

        log.info("Фильм с id = {} обновлен.", id);

        return film;
    }

    @Override
    public Film deleteFilm(Long id) {

        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }

        return films.remove(id);
    }

    public List<Film> getPopularFilms(int count) {

        return films.values()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes()
                        .size()).limit(count)
                .collect(Collectors.toList());
    }
}
