package ru.yandex.practicum.dao;

import ru.yandex.practicum.models.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa getMpaById(int id);

    List<Mpa> getAllMpas();

    Boolean mpaDoesNotExist(int id);

    String getMpaNameById(int id);
}
