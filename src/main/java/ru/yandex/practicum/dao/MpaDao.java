package ru.yandex.practicum.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface MpaDao {
    SqlRowSet getMpaById(int id);

    SqlRowSet getAllMpas();

    Boolean mpaDoesNotExist(int id);
}
