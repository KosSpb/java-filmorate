package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getGenresList() {
        return genreStorage.getGenresList();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id).orElseThrow(() -> {
            log.info("getGenreById - genre id not found: {}", id);
            throw new NotFoundException("Жанра с данным id не существует.");
        });
    }
}
