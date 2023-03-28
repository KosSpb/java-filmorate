package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final ValidationService validationService;
    private final FilmRepository filmRepository;

    @GetMapping //получение всех фильмов
    private Collection<Film> getAllFilms() {
        return filmRepository.getFilms().values();
    }

    @PostMapping //добавление фильма
    private Film createFilm(@RequestBody Film film) {
        validationService.validateFilm(film);
        filmRepository.createFilm(film);
        log.info("createFilm: film \"{}\" with id {} was created.", film.getName(), film.getId());
        return film;
    }

    @PutMapping //обновление фильма
    private Film updateFilm(@RequestBody Film film) {
        validationService.validateFilm(film);
        filmRepository.updateFilm(film);
        log.info("updateFilm: film \"{}\" with id {} was updated.", film.getName(), film.getId());
        return film;
    }
}
