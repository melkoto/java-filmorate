package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.models.Mpa;

public interface MpaDao {
    Mpa getMpaById(int id);

    SqlRowSet getAllMpas();

    Boolean mpaDoesNotExist(int id);

    SqlRowSet getMpaNameById(int id);
}
