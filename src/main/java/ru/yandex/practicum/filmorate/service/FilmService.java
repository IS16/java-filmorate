package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.storage.Films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private FilmStorage storage;
    private MpaStorage mpaStorage;
    private GenresStorage genresStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage storage, @Qualifier("MpaDbStorage") MpaStorage mpaStorage, @Qualifier("GenresDbStorage") GenresStorage genresStorage) {
        this.storage = storage;
        this.mpaStorage = mpaStorage;
        this.genresStorage = genresStorage;
    }

    public ArrayList<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return storage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        processFilm(film);
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        processFilm(film);
        return storage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);

        if (storage.getLikesByFilm(filmId).contains(userId)) {
            throw new LikeAlreadyExistsException("Пользователь уже поставил лайк данному фильму");
        }

        storage.addLike(filmId, userId);

        log.info("Пользователь ID = " + userId + " поставил лайк фильму " + film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);

        if (!storage.getLikesByFilm(filmId).contains(userId)) {
            throw new LikeNotFoundException("Лайк фильму от данного пользователя не найден");
        }

        storage.deleteLike(filmId, userId);

        log.info("Пользователь ID = " + userId + " удалил свой лайк у фильма " + film);
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getPopularFilms(count);
    }

    public ArrayList<Genre> getAllGenres() {
        return genresStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genresStorage.getGenreById(id);
    }

    public ArrayList<Rate> getAllRates() {
        return mpaStorage.getAllRates();
    }

    public Rate getRateById(int id) {
        return mpaStorage.getRateById(id);
    }

    private void processFilm(Film film) {
        if (film.getMpa() != null) {
            film.setMpa(mpaStorage.getRateById(film.getMpa().getId()));
        }

        film.filterDuplicateGenres();
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            ArrayList<Genre> genres = new ArrayList<>();

            film.getGenres().forEach(item -> genres.add(genresStorage.getGenreById(item.getId())));

            film.setGenres(genres);
        } else if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
    }
}
