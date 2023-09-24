package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage storage;

    private final Comparator<Film> filmsComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            if (Objects.equals(o1.getId(), o2.getId())) {
                return 0;
            }

            return o2.getLikes().size() - o1.getLikes().size();
        }
    };

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            throw new LikeAlreadyExistsException("Пользователь уже поставил лайк данному фильму");
        }

        film.addLike(userId);
        log.info("Пользователь ID = " + userId + " поставил лайк фильму " + film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);

        if (!film.getLikes().contains(userId)) {
            throw new LikeNotFoundException("Лайк фильму от данного пользователя не найден");
        }

        film.deleteLike(userId);
        log.info("Пользователь ID = " + userId + " удалил свой лайк у фильма " + film);
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getAllFilms().stream()
                .sorted(filmsComparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}
