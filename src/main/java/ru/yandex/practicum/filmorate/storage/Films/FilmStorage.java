package ru.yandex.practicum.filmorate.storage.Films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {
    ArrayList<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Integer> getLikesByFilm(int filmId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}
