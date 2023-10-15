package ru.yandex.practicum.filmorate.storage.Genres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component("InMemoryGenresStorage")
public class InMemoryGenresStorage implements GenresStorage {
    private final HashMap<Integer, Genre> genres = new HashMap<>();

    public InMemoryGenresStorage() {
        genres.put(1, new Genre(1, "Комедия"));
        genres.put(2, new Genre(2, "Драма"));
        genres.put(3, new Genre(3, "Мультфильм"));
        genres.put(4, new Genre(4, "Триллер"));
        genres.put(5, new Genre(5, "Документальный"));
        genres.put(6, new Genre(6, "Боевик"));
    }

    @Override
    public ArrayList<Genre> getAllGenres() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Genre getGenreById(int id) {
        if (!genres.containsKey(id)) {
            log.warn("Жанр с идентификатором {} не найден.", id);
            throw new GenreNotFoundException(String.format("Жанр с идентификатором {} не найден.", id));
        }

        log.info("Найден жанр: {} {}", genres.get(id).getId(), genres.get(id).getName());
        return genres.get(id);
    }
}
