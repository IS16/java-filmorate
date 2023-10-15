package ru.yandex.practicum.filmorate.storage.Films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {
    ArrayList<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Integer> getLikesByFilm(int film_id);
    void addLike(int film_id, int user_id);
    void deleteLike(int film_id, int user_id);
    List<Film> getPopularFilms(int count);
}
