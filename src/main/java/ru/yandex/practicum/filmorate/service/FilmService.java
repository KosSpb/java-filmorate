package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void createFilm(Film film) {
        filmStorage.createFilm(film);
        log.info("createFilm: film \"{}\" with id {} was created.", film.getName(), film.getId());
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
        log.info("updateFilm: film \"{}\" with id {} was updated.", film.getName(), film.getId());
    }

    public Collection<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLikeToFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (!userStorage.getUsersList().contains(userStorage.getUserById(userId))) {
            log.info("addLikeToFilm - user id not found: {}", userId);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }

        if (film.getLikes().add(userId)) {
            log.info("addLikeToFilm: film with id {} was liked by user with id {}", id, userId);
        } else {
            log.info("addLikeToFilm - film with id {} was already liked by user with id {}", id, userId);
            throw new AlreadyExistException("Вы уже ставили лайк фильму c данным id");
        }
        return film;
    }

    public Film removeLikeOfFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (!userStorage.getUsersList().contains(userStorage.getUserById(userId))) {
            log.info("removeLikeOfFilm - user id not found: {}", userId);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }

        if (film.getLikes().remove(userId)) {
            log.info("removeLikeOfFilm: like of film with id {} was removed by user with id {}", id, userId);
        } else {
            log.info("removeLikeOfFilm: like of film with id {} was already removed by user with id {}", id, userId);
            throw new NotFoundException("Вы уже удалили лайк у фильма c данным id.");
        }
        return film;
    }

    public Collection<Film> getTopFilmsList(int count) {
        return filmStorage.getFilmsList().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
