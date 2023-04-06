package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final ValidationService validationService;
    private final FilmService filmService;

    @Autowired
    public FilmController(ValidationService validationService, FilmService filmService) {
        this.validationService = validationService;
        this.filmService = filmService;
    }

    @GetMapping //получение всех фильмов
    public Collection<Film> getAllFilms() {
        return filmService.getFilmsList();
    }

    @PostMapping //добавление фильма
    public Film createFilm(@RequestBody Film film) {
        validationService.validateFilm(film);
        filmService.createFilm(film);
        return film;
    }

    @PutMapping //обновление фильма
    public Film updateFilm(@RequestBody Film film) {
        validationService.validateFilm(film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/{id}") //получение фильма по идентификатору
    public Film getFilmById(@PathVariable(value = "id") int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}") //пользователь ставит лайк фильму
    public Film addLikeToFilm(@PathVariable(value = "id") int id,
                              @PathVariable(value = "userId") int userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") //пользователь удаляет лайк
    public Film removeLikeOfFilm(@PathVariable(value = "id") int id,
                                 @PathVariable(value = "userId") int userId) {
        return filmService.removeLikeOfFilm(id, userId);
    }

    @GetMapping("/popular") //возвращает список из первых count фильмов по количеству лайков
    public Collection<Film> getTopFilmsList(@RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.getTopFilmsList(count);
    }
}
