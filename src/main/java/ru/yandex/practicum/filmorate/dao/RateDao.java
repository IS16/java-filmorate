package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;
import java.util.Optional;

public interface RateDao {
    Optional<Rate> findRateById(int id);
    ArrayList<Rate> getAllRates();
}
