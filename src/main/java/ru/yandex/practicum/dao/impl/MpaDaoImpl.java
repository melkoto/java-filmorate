package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.exceptions.BadRequestException;
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
        SqlRowSet mpa = jdbcTemplate.queryForRowSet(sql, id);

        if (mpa.next()) {
            Mpa m = new Mpa();
            m.setId(mpa.getInt("id"));
            m.setName(mpa.getString("name"));
            return m;
        } else {
            throw new BadRequestException("Mpa с id " + id + " не найден");
        }
    }

    @Override
    public List<Mpa> getAllMpas() {
        List<Mpa> result = new ArrayList<>();
        String sql = "SELECT id FROM mpas ORDER BY id";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class);
        for (Integer id : ids) {
            result.add(getMpaById(id));
        }
        return result;
    }
}
