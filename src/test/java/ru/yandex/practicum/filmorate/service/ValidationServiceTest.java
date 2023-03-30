package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {
    private ValidationService validationService;
    private Film film;
    private User user;

    @BeforeEach
    void beforeEach() {
        validationService = new ValidationService();
        film = new Film(0, "filmName", "filmDescription",
                LocalDate.of(1999, 12, 31), 1);
        user = new User(0, "user@ya.ru", "userLogin", "userName",
                LocalDate.of(1999, 1, 1));
    }

    @Test
    void validateFilmTestWithBlankName() {
        film.setName("");

        assertThrows(ValidationException.class, () -> validationService.validateFilm(film));
    }

    @Test
    void validateFilmTestWithDescriptionLengthOver200symbols() {
        film.setDescription("UmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurman" +
                "UmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurmanUmaThurman" +
                "UmaThurmanUmaThurmanUmaThurmanU");

        assertEquals(201, film.getDescription().length(), "Количество символов описания не совпадает.");
        assertThrows(ValidationException.class, () -> validationService.validateFilm(film));
    }

    @Test
    void validateFilmTestWithReleaseDateBefore28_12_1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class, () -> validationService.validateFilm(film));
    }

    @Test
    void validateFilmTestWithZeroDuration() {
        film.setDuration(0);

        assertThrows(ValidationException.class, () -> validationService.validateFilm(film));
    }

    @Test
    void validateFilmTestWithNegativeDuration() {
        film.setDuration(-1);

        assertThrows(ValidationException.class, () -> validationService.validateFilm(film));
    }

    @Test
    void validateUserTestWithBlankEmail() {
        user.setEmail("");

        assertThrows(ValidationException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUserTestWithEmailWithoutCharacterAt() {
        user.setEmail("yandex.ru");

        assertThrows(ValidationException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUserTestWithBlankLogin() {
        user.setLogin("");

        assertThrows(ValidationException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUserTestWithLoginContainsSpaceCharacter() {
        user.setLogin("Login With Space");

        assertThrows(ValidationException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUserTestWithBirthdayDateInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUserTestWithNullName() {
        user.setName(null);
        validationService.validateUser(user);

        assertEquals(user.getLogin(), user.getName(), "Имя не совпадает с логином.");
    }
}