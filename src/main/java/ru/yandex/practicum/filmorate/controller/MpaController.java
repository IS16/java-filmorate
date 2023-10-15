package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private FilmService service;

    @Autowired
    MpaController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<Rate> getAllates() {
        return service.getAllRates();
    }

    @GetMapping("/{id}")
    public Rate getGenreById(@PathVariable int id) {
        return service.getRateById(id);
    }
}
