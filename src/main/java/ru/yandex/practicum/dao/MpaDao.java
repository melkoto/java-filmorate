package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa getMpaById(int id);

    List<Mpa> getAllMpas();

    Boolean mpaDoesNotExist(int id);

    SqlRowSet getMpaNameById(int id);
}
