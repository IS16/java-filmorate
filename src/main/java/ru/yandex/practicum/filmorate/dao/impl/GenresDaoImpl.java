package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class GenresDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", id);

        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );

            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Genre> getAllGenres() {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");

        ArrayList<Genre> genres = new ArrayList<>();

        while (genreRows.next()) {
            genres.add(new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            ));
        }

        return genres;
    }

    @Override
    public ArrayList<Genre> getFilmGenres(int film_id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE film_id = ?", film_id);

        ArrayList<Genre> genres = new ArrayList<>();

        while (genreRows.next()) {
            genres.add(findGenreById(genreRows.getInt("genre_id")).get());
        }

        return genres;
    }
}
