package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private FilmService service;

    @Autowired
    GenreController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<Genre> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return service.getGenreById(id);
    }
}
