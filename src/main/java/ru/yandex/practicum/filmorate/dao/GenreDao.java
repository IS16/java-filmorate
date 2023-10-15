package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findGenreById(int id);

    ArrayList<Genre> getAllGenres();

    ArrayList<Genre> getFilmGenres(int filmId);
}
