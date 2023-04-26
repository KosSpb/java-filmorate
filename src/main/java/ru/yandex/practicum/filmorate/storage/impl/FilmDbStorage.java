package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

        film.setId(keyHolder.getKey().longValue());

        addGenresToDb(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            return Optional.empty();
        }
        String sqlQueryDelGenres = "delete from film_genre " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQueryDelGenres, film.getId());

        addGenresToDb(film);

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

        return Optional.of(film);
    }

    @Override
    public Collection<Film> getFilmsList() {
        String sqlQuery = "select * from film f " +
                "join " +
                "mpa_rating mr on f.mpa_rating_id = mr.mpa_rating_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);

        Map<Long, List<Genre>> genres = addGenresToSeveralFilms(films);
        if (!genres.isEmpty()) {
            for (Film film : films) {
                if (genres.get(film.getId()) != null) {
                    film.setGenres(new LinkedHashSet<>(genres.get(film.getId())));
                }
            }
        }
        return films;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        String sqlQuery = "select * from film f " +
                "join " +
                "mpa_rating mr on f.mpa_rating_id = mr.mpa_rating_id " +
                "where film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

            film.setGenres(new LinkedHashSet<>(addGenresToFilm(film.getId())));
            return Optional.of(film);
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
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);

        Map<Long, List<Genre>> genres = addGenresToSeveralFilms(films);
        if (!genres.isEmpty()) {
            for (Film film : films) {
                if (genres.get(film.getId()) != null) {
                    film.setGenres(new LinkedHashSet<>(genres.get(film.getId())));
                }
            }
        }
        return films;
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
                .genres(new LinkedHashSet<>())
                .build();
    }

    private Collection<Genre> addGenresToFilm(long id) {
        String sqlQuery = "select * from genre " +
                "where genre_id in " +
                "(select genre_id from film_genre " +
                "where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, genreDbStorage::mapRowToGenre, id);
    }

    private Map<Long, List<Genre>> addGenresToSeveralFilms(List<Film> films) {
        if (films.isEmpty()) {
            return new HashMap<>();
        }
        StringBuilder queryBuilder = new StringBuilder();
        for (Film film : films) {
            queryBuilder.append(film.getId());
            queryBuilder.append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        String sqlQuery = "select * from film_genre fg " +
                "join " +
                "genre g on fg.genre_id = g.genre_id " +
                "where film_id in " +
                "(" + queryBuilder + ")";

        SqlRowSet filmsIdsAndGenresRows = jdbcTemplate.queryForRowSet(sqlQuery);
        Map<Long, List<Genre>> genresOfFilms = new HashMap<>();

        while (filmsIdsAndGenresRows.next()) {
            long id = filmsIdsAndGenresRows.getLong("film_id");
            Genre genre = new Genre(filmsIdsAndGenresRows.getInt("genre_id"),
                    filmsIdsAndGenresRows.getString("genre_name"));

            List<Genre> genresOfEachFilm;
            if (genresOfFilms.containsKey(id)) {
                genresOfEachFilm = genresOfFilms.get(id);
            } else {
                genresOfEachFilm = new ArrayList<>();
                genresOfFilms.put(id, genresOfEachFilm);
            }
            genresOfEachFilm.add(genre);
        }
        return genresOfFilms;
    }

    private void addGenresToDb(Film film) {
        if (film.getGenres().size() > 0) {
            String sqlQueryInsGenres = "insert into film_genre(film_id, genre_id) " +
                    "values (?, ?)";
            List<Genre> genres = new ArrayList<>(film.getGenres());

            jdbcTemplate.batchUpdate(sqlQueryInsGenres,
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, film.getId());
                            ps.setLong(2, genres.get(i).getId());
                        }

                        public int getBatchSize() {
                            return genres.size();
                        }
                    });
        }
    }
}
