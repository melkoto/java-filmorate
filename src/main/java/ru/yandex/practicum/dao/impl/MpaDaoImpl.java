package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.models.Mpa;

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
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Mpa mpa = new Mpa();
            mpa.setId(resultSet.getInt("id"));
            mpa.setName(resultSet.getString("name"));
            return mpa;
        });
    }

    @Override
    public Boolean mpaDoesNotExist(int id) {
        return !jdbcTemplate.queryForRowSet("SELECT * FROM mpas WHERE id =?", new Object[]{id}).next();
    }

    @Override
    public Mpa getMpaByFilmId(Long filmId) {
        String sql = "SELECT mpa_id, m.name FROM FILMS as f " +
                "LEFT JOIN mpas m on f.mpa_id = m.ID " +
                "WHERE f.ID = ? ORDER BY f.mpa_id";

        SqlRowSet mpa = jdbcTemplate.queryForRowSet(sql, filmId);

        if (mpa.next()) {
            Mpa m = new Mpa();
            m.setId(mpa.getInt("mpa_id"));
            m.setName(mpa.getString("name"));
            return m;
        }

        return null;
    }
}
