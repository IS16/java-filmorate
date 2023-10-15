package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RateDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final RateDao rateDao;
    private final GenreDao genreDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, RateDao rateDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.rateDao = rateDao;
        this.genreDao = genreDao;
    }

    @Override
    public Optional<Rate> findRateById(int id) {
        return rateDao.findRateById(id);
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");

        ArrayList<Film> films = new ArrayList<>();

        while (filmRows.next()) {

            films.add(new Film(
                filmRows.getInt("id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                filmRows.getDate("releaseDate").toLocalDate(),
                filmRows.getInt("duration"),
                rateDao.findRateById(filmRows.getInt("rate_id")).get(),
                genreDao.getFilmGenres(filmRows.getInt("id"))
            ));
        }

        return films;
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", id);

        if (filmRows.next()) {
            Film film = new Film(
                filmRows.getInt("id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                filmRows.getDate("releaseDate").toLocalDate(),
                filmRows.getInt("duration"),
                rateDao.findRateById(filmRows.getInt("rate_id")).get(),
                genreDao.getFilmGenres(filmRows.getInt("id"))
            );

            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, rate_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pr = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, film.getName());
            pr.setString(2, film.getDescription());
            pr.setDate(3, Date.valueOf(film.getReleaseDate()));
            pr.setInt(4, film.getDuration());
            pr.setInt(5, film.getMpa().getId());
            return pr;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addNewGenresToFilm(film.getId(), film.getGenres());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rate_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        sqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addNewGenresToFilm(film.getId(), film.getGenres());
        }

        return film;
    }

    @Override
    public ArrayList<Integer> getLikesByFilm(int filmId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE film_id = ?", filmId);

        ArrayList<Integer> likes = new ArrayList<>();

        while (likeRows.next()) {
            likes.add(likeRows.getInt("user_id"));
        }

        return likes;
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userId
        );
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT id FROM films LEFT OUTER JOIN likes ON likes.film_id = films.id GROUP BY id ORDER BY COUNT(likes.user_id) DESC LIMIT ?", count);

        List<Film> films = new ArrayList<>();

        while (filmRows.next()) {
            films.add(findFilmById(filmRows.getInt("id")).get());
        }

        return films;
    }

    private void addNewGenresToFilm(int filmId, ArrayList<Genre> genres) {
        for (Genre item : genres) {
            String sqlQuery1 = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

            jdbcTemplate.update(sqlQuery1,
                    filmId,
                    item.getId()
            );
        }
    }
}
