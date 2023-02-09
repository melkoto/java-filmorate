package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.models.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM mpas WHERE id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (!rowSet.next()) {
            return null;
        }

        Mpa mpa = new Mpa();
        mpa.setId(rowSet.getInt("id"));
        mpa.setName(rowSet.getString("name"));

        return mpa;
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sql = "SELECT * FROM mpas ORDER BY id";
        List<Mpa> mpas = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(rowSet.getInt("id"));
            mpa.setName(rowSet.getString("name"));
            mpas.add(mpa);
        }

        return mpas;
    }

    @Override
    public Boolean mpaDoesNotExist(int id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM mpas WHERE id =?", new Object[]{id}).next();
    }

    @Override
    public SqlRowSet getMpaNameById(int id) {
        return jdbcTemplate.queryForRowSet("SELECT name FROM mpas WHERE id = ?", id);
    }
}
