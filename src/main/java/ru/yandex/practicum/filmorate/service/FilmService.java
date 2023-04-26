package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public void createFilm(Film film) {
        filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film).orElseThrow(() -> {
            log.info("updateFilm - film id not found: {}", film);
            throw new NotFoundException("Фильма с данным id не существует.");
        });
    }

    public Collection<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id).orElseThrow(() -> {
            log.info("getFilmById - film id not found: {}", id);
            throw new NotFoundException("Фильма с данным id не существует.");
        });
    }

    public Film addLikeToFilm(long id, long userId) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> {
            log.info("addLikeToFilm - film id not found: {}", id);
            throw new NotFoundException("Фильма с данным id не существует.");
        });

        if (userStorage.getUserById(userId).isEmpty()) {
            log.info("addLikeToFilm - user id not found: {}", userId);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }

        if (likesStorage.addLikeToFilm(id, userId) != 0) {
            log.info("addLikeToFilm: film with id {} was liked by user with id {}", id, userId);
        } else {
            log.info("addLikeToFilm - film with id {} was already liked by user with id {}", id, userId);
            throw new AlreadyExistException("Вы уже ставили лайк фильму c данным id");
        }

        return film;
    }

    public Film removeLikeOfFilm(long id, long userId) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> {
            log.info("removeLikeOfFilm - film id not found: {}", id);
            throw new NotFoundException("Фильма с данным id не существует.");
        });

        if (userStorage.getUserById(userId).isEmpty()) {
            log.info("removeLikeOfFilm - user id not found: {}", userId);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }

        if (likesStorage.removeLikeOfFilm(id, userId) != 0) {
            log.info("removeLikeOfFilm: like of film with id {} was removed by user with id {}", id, userId);
        } else {
            log.info("removeLikeOfFilm: like of film with id {} was already removed by user with id {}", id, userId);
            throw new NotFoundException("Вы уже удалили лайк у фильма c данным id.");
        }

        return film;
    }

    public Collection<Film> getTopFilmsList(long count) {
        return filmStorage.getTopFilmsList(count);
    }
}
