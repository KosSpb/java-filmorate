package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int generatedId;
    private final Map<Integer, Film> films = new HashMap<>();

    private int generateId() {
        return ++generatedId;
    }

    @Override
    public void createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("updateFilm - film id not found: {}", film);
            throw new NotFoundException("Фильма с данным id не существует.");
        }
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> getFilmsList() {
        return films.values();
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.info("getFilmById - film id not found: {}", id);
            throw new NotFoundException("Фильма с данным id не существует.");
        }
        return films.get(id);
    }
}
