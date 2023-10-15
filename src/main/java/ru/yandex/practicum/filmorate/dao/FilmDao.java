package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Rate> findRateById(int id);
    ArrayList<Film> getAllFilms();
    Optional<Film> findFilmById(int id);
    Film createFilm(Film film);
    Film updateFilm(Film film);
    ArrayList<Integer> getLikesByFilm(int filmId);
    void addLike(int film_id, int userId);
    void deleteLike(int film_id, int userId);
    List<Film> getPopularFilms(int count);
}
