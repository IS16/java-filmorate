package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

public interface FilmStorage {
    int currentId = 0;
    HashMap<Integer, Film> films = null;

    public ArrayList<Film> getAllFilms();

    public Film getFilmById(int filmId);

    public Film addFilm(Film film);

    public Film updateFilm(Film film);
}
