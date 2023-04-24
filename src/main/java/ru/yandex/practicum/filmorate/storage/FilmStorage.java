package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Collection<Film> getFilmsList();

    Optional<Film> getFilmById(long id);

    Collection<Film> getTopFilmsList(long count);
}
