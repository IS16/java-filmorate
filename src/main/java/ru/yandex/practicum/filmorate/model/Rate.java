package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Rate {
    private Integer id;
    private String name = "";

    public Rate (int id, String name) {
        this.id = id;
        this.name = name;
    }
}