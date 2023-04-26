package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    int addLikeToFilm(long id, long userId);

    int removeLikeOfFilm(long id, long userId);
}
