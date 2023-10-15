package ru.yandex.practicum.filmorate.storage.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RateDao;
import ru.yandex.practicum.filmorate.exception.RateNotFoundException;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final RateDao rateDao;

    public MpaDbStorage(RateDao rateDao) {
        this.rateDao = rateDao;
    }

    @Override
    public ArrayList<Rate> getAllRates() {
        return rateDao.getAllRates();
    }

    @Override
    public Rate getRateById(int id) {
        Optional<Rate> rate = rateDao.findRateById(id);
        if (rate.isEmpty()) {
            log.warn("Рейтинг с идентификатором {} не найден.", id);
            throw new RateNotFoundException(String.format("Рейтинг с идентификатором {} не найден.", id));
        }

        log.info("Найден рейтинг: {} {}", rate.get().getId(), rate.get().getName());
        return rate.get();
    }
}
