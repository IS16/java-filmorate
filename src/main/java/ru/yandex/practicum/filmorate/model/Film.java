package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;

    @NotBlank
    private final String name;

    @Size(max = 200)
    private final String description;

    private LocalDate releaseDate;

    @Positive
    private final int duration;

    private Rate mpa;
    private ArrayList<Genre> genres = new ArrayList<>();

    private Set<Integer> likes = new HashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(int userId) {
        likes.remove(userId);
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Rate mpa, ArrayList<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public void filterDuplicateGenres() {
        if (this.genres == null || this.genres.isEmpty()) {
            return;
        }

        ArrayList<Genre> genres = new ArrayList<>();

        this.genres.forEach(item -> {
            if (!genres.contains(item)) {
                genres.add(item);
            }
        });

        this.genres = genres;
    }
}
