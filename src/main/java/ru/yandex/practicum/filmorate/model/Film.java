package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    public static int currentId = 0;

    private Integer id;

    @NotBlank
    private final String name;

    @Size(max=200)
    private final String description;

    private final LocalDate releaseDate;

    @Positive
    private final int duration;
}
