package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"classpath:testDataBeforeMethod.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"classpath:testDataAfterMethod.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FriendshipStorage friendshipStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final MpaRatingStorage mpaRatingStorage;

    @Test
    void createFilmTest() {
        filmStorage.createFilm(new Film(3, "TestFilmName", "TestFilmDescription",
                LocalDate.of(2023, 1, 21), 60, new MpaRating(1, "G"),
                new LinkedHashSet<>()));

        Optional<Film> filmOptional = filmStorage.getFilmById(3);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "TestFilmName")
                                .hasFieldOrPropertyWithValue("description", "TestFilmDescription")
                                .hasFieldOrPropertyWithValue("duration", 60)
                );
    }

    @Test
    void updateFilmTest() {
        Film filmForUpdate = filmStorage.getFilmById(1).get();
        filmForUpdate.setName("UpdatedFilm");
        filmForUpdate.setDescription("UpdatedDescription");

        Optional<Film> filmOptional = filmStorage.updateFilm(filmForUpdate);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "UpdatedFilm")
                                .hasFieldOrPropertyWithValue("description", "UpdatedDescription")
                                .hasFieldOrPropertyWithValue("duration", 100)
                );
    }

    @Test
    void getFilmsListTest() {
        Collection<Film> films = filmStorage.getFilmsList();

        assertThat(films).size()
                .isEqualTo(2);
    }

    @Test
    void getFilmByIdTest() {
        Optional<Film> filmOptional = filmStorage.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "bestFilm")
                                .hasFieldOrPropertyWithValue("description", "bestDescription")
                                .hasFieldOrPropertyWithValue("duration", 100)
                );
    }

    @Test
    void getTopFilmsListTest() {
        Film film = filmStorage.getFilmById(1).get();
        User user = userStorage.getUserById(1).get();
        likesStorage.addLikeToFilm(film.getId(), user.getId());
        List<Film> topFilms = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(topFilms).size()
                .isEqualTo(2);
        assertThat(topFilms.get(0))
                .hasFieldOrPropertyWithValue("name", "bestFilm")
                .hasFieldOrPropertyWithValue("description", "bestDescription")
                .hasFieldOrPropertyWithValue("duration", 100);
    }

    @Test
    void addFriendTest() {
        int shouldBeMoreThen0 = friendshipStorage.addFriend(1, 3);
        List<User> friends = new ArrayList<>(friendshipStorage.getFriendsList(1));

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
        assertThat(friends).size()
                .isEqualTo(2);
        assertThat(friends.get(1))
                .hasFieldOrPropertyWithValue("email", "person3@ya.ru")
                .hasFieldOrPropertyWithValue("login", "person3Login");
    }

    @Test
    void acceptFriendshipTest() {
        int shouldBeMoreThen0 = friendshipStorage.acceptFriendship(1, 2);

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
    }

    @Test
    void removeFriendTest() {
        int shouldBeMoreThen0 = friendshipStorage.removeFriend(1, 2);
        List<User> friends = new ArrayList<>(friendshipStorage.getFriendsList(1));

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
        assertThat(friends).size()
                .isEqualTo(0);
    }

    @Test
    void getFriendsListTest() {
        List<User> friends = new ArrayList<>(friendshipStorage.getFriendsList(1));

        assertThat(friends).size()
                .isEqualTo(1);
        assertThat(friends.get(0))
                .hasFieldOrPropertyWithValue("email", "person2@ya.ru")
                .hasFieldOrPropertyWithValue("login", "person2Login");
    }

    @Test
    void getMutualFriendsTest() {
        List<User> mutualFriends = new ArrayList<>(friendshipStorage.getMutualFriends(1, 3));

        assertThat(mutualFriends).size()
                .isEqualTo(1);
        assertThat(mutualFriends.get(0))
                .hasFieldOrPropertyWithValue("email", "person2@ya.ru")
                .hasFieldOrPropertyWithValue("login", "person2Login");
    }

    @Test
    void getGenresListTest() {
        Collection<Genre> genres = genreStorage.getGenresList();

        assertThat(genres).size()
                .isEqualTo(6);
    }

    @Test
    void getGenreByIdTest() {
        Optional<Genre> genreOptional = genreStorage.getGenreById(1);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    void addLikeToFilmTest() {
        Film film = filmStorage.getFilmById(2).get();
        User user = userStorage.getUserById(1).get();
        List<Film> topFilms = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(topFilms).size()
                .isEqualTo(2);
        assertThat(topFilms.get(0))
                .hasFieldOrPropertyWithValue("name", "bestFilm")
                .hasFieldOrPropertyWithValue("description", "bestDescription");

        int shouldBeMoreThen0 = likesStorage.addLikeToFilm(film.getId(), user.getId());
        List<Film> topFilmsAfterLike = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
        assertThat(topFilmsAfterLike.get(0))
                .hasFieldOrPropertyWithValue("name", "boringFilm")
                .hasFieldOrPropertyWithValue("description", "boringDescription");
    }

    @Test
    void removeLikeOfFilm() {
        Film film = filmStorage.getFilmById(2).get();
        User user = userStorage.getUserById(1).get();
        List<Film> topFilms = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(topFilms).size()
                .isEqualTo(2);
        assertThat(topFilms.get(0))
                .hasFieldOrPropertyWithValue("name", "bestFilm")
                .hasFieldOrPropertyWithValue("description", "bestDescription");

        likesStorage.addLikeToFilm(film.getId(), user.getId());
        List<Film> topFilmsAfterLike = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(topFilmsAfterLike.get(0))
                .hasFieldOrPropertyWithValue("name", "boringFilm")
                .hasFieldOrPropertyWithValue("description", "boringDescription");

        int shouldBeMoreThen0 = likesStorage.removeLikeOfFilm(film.getId(), user.getId());
        List<Film> topFilmsAfterDeleteLike = new ArrayList<>(filmStorage.getTopFilmsList(2));

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
        assertThat(topFilmsAfterDeleteLike.get(0))
                .hasFieldOrPropertyWithValue("name", "bestFilm")
                .hasFieldOrPropertyWithValue("description", "bestDescription");
    }

    @Test
    void getMpaRatingsListTest() {
        Collection<MpaRating> mpas = mpaRatingStorage.getMpaRatingsList();

        assertThat(mpas).size()
                .isEqualTo(5);
    }

    @Test
    void getMpaRatingByIdTest() {
        Optional<MpaRating> mpaOptional = mpaRatingStorage.getMpaRatingById(1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpaRating ->
                        assertThat(mpaRating)
                                .hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    void createUserTest() {
        userStorage.createUser(new User(4, "testUser@ya.ru", "TestLogin", "TestName",
                LocalDate.of(200, 1, 1)));

        Optional<User> userOptional = userStorage.getUserById(4);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "testUser@ya.ru")
                                .hasFieldOrPropertyWithValue("login", "TestLogin")
                                .hasFieldOrPropertyWithValue("name", "TestName")
                );
    }

    @Test
    void updateUserTest() {
        User userForUpdate = userStorage.getUserById(1).get();
        userForUpdate.setEmail("updatedEmail");
        userForUpdate.setLogin("UpdatedLogin");
        int shouldBeMoreThen0 = userStorage.updateUser(userForUpdate);

        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(shouldBeMoreThen0)
                .isNotEqualTo(0);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "updatedEmail")
                                .hasFieldOrPropertyWithValue("login", "UpdatedLogin")
                );
    }

    @Test
    void getUsersListTest() {
        Collection<User> users = userStorage.getUsersList();

        assertThat(users).size()
                .isEqualTo(3);
    }

    @Test
    void getUserByIdTest() {
        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("email", "person1@ya.ru")
                                .hasFieldOrPropertyWithValue("login", "person1Login")
                                .hasFieldOrPropertyWithValue("name", "person1Name")
                );
    }
}