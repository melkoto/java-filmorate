package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;

@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SqlRowSet getMpaById(int id) {
        String sql = "SELECT * FROM mpas WHERE id = ?";
        return jdbcTemplate.queryForRowSet(sql, id);
    }

    @Override
    public SqlRowSet getAllMpas() {
        String sql = "SELECT * FROM mpas ORDER BY id";
        return jdbcTemplate.queryForRowSet(sql);
    }

    @Override
    public Boolean mpaDoesNotExist(int id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM mpas WHERE id =?", new Object[]{id}).next();
    }
}
