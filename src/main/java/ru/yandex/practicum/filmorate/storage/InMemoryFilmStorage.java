package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int currentId = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.warn(String.format("Фильм с данным ID (id = %d) не найден", id));
            throw new FilmNotFoundException(String.format("Фильм с данным ID (id = %d) не найден", id));
        }

        return films.get(id);
    }

    public Film addFilm(Film film) {
        validateFilm(film);

        if (film.getId() == null) {
            film.setId(generateId());
        }

        films.put(film.getId(), film);

        log.info("Добавлен фильм: " + film + ". Количество фильмов в библиотеке: " + films.size());

        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn(String.format("Фильм с данным ID (id = %d) не найден", film.getId()));
            throw new FilmNotFoundException(String.format("Фильм с данным ID (id = %d) не найден", film.getId()));
        }

        validateFilm(film);

        films.put(film.getId(), film);
        log.info("Обновлён фильм: " + film);
        return film;
    }

    private int generateId() {
        return ++currentId;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выхода фильма должна быть не раньше 28.12.1895");
            throw new ValidationException("Дата выхода фильма должна быть не раньше 28.12.1895");
        }
    }
}
