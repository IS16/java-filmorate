package ru.yandex.practicum.filmorate.storage.Genres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component("GenresDbStorage")
public class GenresDbStorage implements GenresStorage {
    private final GenreDao genreDao;

    public GenresDbStorage(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public ArrayList<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Override
    public Genre getGenreById(int id) {
        Optional<Genre> genre = genreDao.findGenreById(id);
        if (genre.isEmpty()) {
            log.warn("Жанр с идентификатором {} не найден.", id);
            throw new GenreNotFoundException(String.format("Жанр с идентификатором {} не найден.", id));
        }

        log.info("Найден жанр: {} {}", genre.get().getId(), genre.get().getName());
        return genre.get();
    }
}
