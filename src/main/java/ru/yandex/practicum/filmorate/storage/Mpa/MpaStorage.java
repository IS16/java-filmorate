package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;

public interface MpaStorage {
    public ArrayList<Rate> getAllRates();

    public Rate getRateById(int id);
}
