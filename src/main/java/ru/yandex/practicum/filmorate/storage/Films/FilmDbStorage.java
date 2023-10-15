package ru.yandex.practicum.filmorate.storage.Films;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final FilmDao filmDao;


    public FilmDbStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public Film getFilmById(int filmId) {
        Optional<Film> film = filmDao.findFilmById(filmId);
        if (film.isEmpty()) {
            log.warn(String.format("Фильм с данным ID (id = %d) не найден", filmId));
            throw new FilmNotFoundException(String.format("Фильм с данным ID (id = %d) не найден", filmId));
        }

        return film.get();
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);

        log.info("Добавлен фильм: " + film);
        return filmDao.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());

        validateFilm(film);

        filmDao.updateFilm(film);
        log.info("Обновлён фильм: " + film);
        return film;
    }

    public ArrayList<Integer> getLikesByFilm(int film_id) {
        return filmDao.getLikesByFilm(film_id);
    }

    @Override
    public void addLike(int film_id, int user_id) {
        filmDao.addLike(film_id, user_id);
    }

    public void deleteLike(int film_id, int user_id) {
        filmDao.deleteLike(film_id, user_id);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmDao.getPopularFilms(count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выхода фильма должна быть не раньше 28.12.1895");
            throw new ValidationException("Дата выхода фильма должна быть не раньше 28.12.1895");
        }
    }
}
