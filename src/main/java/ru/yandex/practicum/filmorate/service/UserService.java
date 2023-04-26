package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        userStorage.createUser(user);
    }

    public void updateUser(User user) {
        if (userStorage.updateUser(user) == 0) {
            log.info("updateUser - user id not found: {}", user);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }
    }

    public Collection<User> getUsersList() {
        return userStorage.getUsersList();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id).orElseThrow(() -> {
            log.info("getUserById - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        });
    }
}
