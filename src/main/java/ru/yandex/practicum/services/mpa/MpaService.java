package ru.yandex.practicum.services.mpa;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Mpa;

import java.util.ArrayList;
import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpaById(int id) {
        if (mpaDoesNotExist(id)) {
            throw new NotFoundException("Mpa с id " + id + " не найден");
        }

        SqlRowSet mpa = mpaDao.getMpaById(id);
        Mpa m = new Mpa();

        if (mpa.next()) {
            m.setId(mpa.getInt("id"));
            m.setName(mpa.getString("name"));
            return m;
        }
        return m;
    }

    public List<Mpa> getAllMpas() {
        SqlRowSet mpas = mpaDao.getAllMpas();
        List<Mpa> mpaList = new ArrayList<>();

        while (mpas.next()) {
            Mpa m = new Mpa();
            m.setId(mpas.getInt("id"));
            m.setName(mpas.getString("name"));
            mpaList.add(m);
        }
        return mpaList;
    }

    public Boolean mpaDoesNotExist(int id) {
        return mpaDao.mpaDoesNotExist(id);
    }

    public String getMpaNameById(int id) {
        SqlRowSet mpa = mpaDao.getMpaNameById(id);

        if (mpa.next()) {
            return mpa.getString("name");
        }

        throw new NotFoundException("Mpa с id " + id + " не найден");
    }
}
