package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping //получение списка всех рейтингов
    public Collection<MpaRating> getAllMpaRatings() {
        return mpaRatingService.getMpaRatingsList();
    }

    @GetMapping("/{id}") //получение рейтинга по идентификатору
    public MpaRating getMpaRatingById(@PathVariable(value = "id") int id) {
        return mpaRatingService.getMpaRatingById(id);
    }
}
