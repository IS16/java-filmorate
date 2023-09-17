package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int currentId = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(generateId());
        }

        validateFilm(film);

        films.put(film.getId(), film);

        log.info("Добавлен фильм: " + film + ". Количество фильмов в библиотеке: " + films.size());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn(String.format("Фильм с данным ID (id = %d) не найден", film.getId()));
            throw new EntityNotFoundException(String.format("Фильм с данным ID (id = %d) не найден", film.getId()));
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
