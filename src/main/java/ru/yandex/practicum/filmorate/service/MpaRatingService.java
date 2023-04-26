package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    @Autowired
    public MpaRatingService(MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public Collection<MpaRating> getMpaRatingsList() {
        return mpaRatingStorage.getMpaRatingsList();
    }

    public MpaRating getMpaRatingById(int id) {
        return mpaRatingStorage.getMpaRatingById(id).orElseThrow(() -> {
            log.info("getMpaRatingById - mpa rating id not found: {}", id);
            throw new NotFoundException("Рейтинга MPA с данным id не существует.");
        });
    }
}
