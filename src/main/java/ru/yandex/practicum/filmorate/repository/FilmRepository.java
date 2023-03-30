package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FilmRepository {
    private int generatedId;
    private final Map<Integer, Film> films = new HashMap<>();

    private int generateId() {
        return ++generatedId;
    }

    public void createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("updateFilm - film id not found: {}", film);
            throw new ValidationException("Фильма с данным id не существует.");
        }
        films.put(film.getId(), film);
    }

    public Collection<Film> getFilmsList() {
        return films.values();
    }
}
