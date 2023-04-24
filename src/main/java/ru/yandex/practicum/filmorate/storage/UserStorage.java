package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    void createUser(User user);

    int updateUser(User user);

    Collection<User> getUsersList();

    Optional<User> getUserById(long id);
}
