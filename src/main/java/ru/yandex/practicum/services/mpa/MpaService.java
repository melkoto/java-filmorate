package ru.yandex.practicum.services.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.models.Mpa;

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

        return mpaDao.getMpaById(id);
    }

    public List<Mpa> getAllMpas() {
        return mpaDao.getAllMpas();
    }

    public Boolean mpaDoesNotExist(int id) {
        return mpaDao.mpaDoesNotExist(id);
    }

    public Mpa getMpaByFilmId(Long filmId) {
        return mpaDao.getMpaByFilmId(filmId);
    }
}
