package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class ValidationService {
    public void validateFilm(Film film) {
        if (film.getName().isBlank()) {
            log.info("film name is blank: {}", film);
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            log.info("film description length > 200: {}", film);
            throw new ValidationException("Максимальная длина описания фильма — 200 символов. " +
                    "Текущая длина описания: " + film.getDescription().length() + " символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("film release date is before 28.12.1895: {}", film);
            throw new ValidationException("Дата релиза не может быть раньше появления самого первого фильма " +
                    "(28 декабря 1895 года). Уточните дату релиза и сделайте новый запрос.");
        } else if (film.getDuration() <= 0) {
            log.info("film duration is not positive: {}", film);
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    public void validateUser(User user) {
        if (user.getEmail().isBlank()) {
            log.info("user e-mail is blank: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой.");
        } else if (!user.getEmail().contains("@")) {
            log.info("user e-mail without '@': {}", user);
            throw new ValidationException("Электронная почта должна содержать символ '@'. Исправьте её и " +
                    "сделайте новый запрос.");
        } else if (user.getLogin().isBlank()) {
            log.info("user login is blank: {}", user);
            throw new ValidationException("Логин не может быть пустым.");
        } else if (user.getLogin().contains(" ")) {
            log.info("user login contains space character: {}", user);
            throw new ValidationException("Логин не может содержать пробелы. Исправьте его и сделайте новый запрос.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("user birthday is in future: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем. Исправьте её и сделайте " +
                    "новый запрос.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("user login set as user name: {}", user);
        }
    }
}
