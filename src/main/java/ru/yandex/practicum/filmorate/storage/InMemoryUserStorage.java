package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int generatedId;
    private final Map<Integer, User> users = new HashMap<>();

    private int generateId() {
        return ++generatedId;
    }

    @Override
    public void createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("updateUser - user id not found: {}", user);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> getUsersList() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            log.info("getUserById - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }
        return users.get(id);
    }
}
