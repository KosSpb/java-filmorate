package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public void createFilm(Film film) {
        String sqlQuery = "insert into film(film_name, description, release_date, duration, mpa_rating_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        if (film.getGenres().size() > 0) {
            String sqlQueryInsGenres = "insert into film_genre(film_id, genre_id) " +
                    "values (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQueryInsGenres, filmId, genre.getId());
            }
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            return Optional.empty();
        }
        String sqlQueryDelGenres = "delete from film_genre " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQueryDelGenres, film.getId());

        if (film.getGenres().size() > 0) {
            String sqlQueryInsGenres = "insert into film_genre(film_id, genre_id) " +
                    "values (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQueryInsGenres, film.getId(), genre.getId());
            }
        }

        String sqlQuery = "update film set " +
                "film_name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        return getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getFilmsList() {
        String sqlQuery = "select * from film f " +
                "join " +
                "mpa_rating mr on f.mpa_rating_id = mr.mpa_rating_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        String sqlQuery = "select * from film f " +
                "join " +
                "mpa_rating mr on f.mpa_rating_id = mr.mpa_rating_id " +
                "where film_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> getTopFilmsList(long count) {
        String sqlQuery = "select * from film f " +
                "join " +
                "mpa_rating mr on f.mpa_rating_id = mr.mpa_rating_id " +
                "left join " +
                "likes l on f.film_id = l.film_id " +
                "group by f.film_id " +
                "order by count(l.user_id) desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new MpaRating(resultSet.getInt("mpa_rating_id"),
                        resultSet.getString("mpa_value")))
                .genres(new LinkedHashSet<>(genresOfFilm(resultSet.getLong("film_id"))))
                .build();
    }

    private Collection<Genre> genresOfFilm(long id) {
        String sqlQuery = "select * from genre " +
                "where genre_id in " +
                "(select genre_id from film_genre " +
                "where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, genreDbStorage::mapRowToGenre, id);
    }
}
