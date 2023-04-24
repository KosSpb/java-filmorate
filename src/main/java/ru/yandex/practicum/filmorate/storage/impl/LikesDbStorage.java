package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Repository
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addLikeToFilm(long id, long userId) {
        String sqlQuery = "insert into likes(film_id, user_id) " +
                "values (?, ?)";
        return jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public int removeLikeOfFilm(long id, long userId) {
        String sqlQuery = "delete from likes " +
                "where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, id, userId);
    }
}
