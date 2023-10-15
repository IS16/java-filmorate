package ru.yandex.practicum.filmorate.storage.Genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;

public interface GenresStorage {
    public ArrayList<Genre> getAllGenres();

    public Genre getGenreById(int id);
}
