package ru.yandex.practicum.filmorate.storage.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.RateNotFoundException;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component("InMemoryMpaStorage")
public class InMemoryMpaStorage implements MpaStorage {
    private final HashMap<Integer, Rate> rates = new HashMap<>();

    public InMemoryMpaStorage() {
        rates.put(1, new Rate(1, "G"));
        rates.put(2, new Rate(2, "PG"));
        rates.put(3, new Rate(3, "PG-13"));
        rates.put(4, new Rate(4, "R"));
        rates.put(5, new Rate(5, "NC-17"));
    }

    @Override
    public ArrayList<Rate> getAllRates() {
        return new ArrayList<>(rates.values());
    }

    @Override
    public Rate getRateById(int id) {
        if (!rates.containsKey(id)) {
            log.warn("Рейтинг с идентификатором {} не найден.", id);
            throw new RateNotFoundException(String.format("Рейтинг с идентификатором {} не найден.", id));
        }

        log.info("Найден рейтинг: {} {}", rates.get(id).getId(), rates.get(id).getName());
        return rates.get(id);
    }
}
