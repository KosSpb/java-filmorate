package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class FilmRepository {
    private int generatedId;
    private final Map<Integer, Film> films = new HashMap<>();

    public int generateId() {
        return ++generatedId;
    }

    public void createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }
}
