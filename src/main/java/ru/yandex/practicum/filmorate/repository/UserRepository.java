package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {
    private int generatedId;
    private final Map<Integer, User> users = new HashMap<>();

    private int generateId() {
        return ++generatedId;
    }

    public void createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("updateUser - user id not found: {}", user);
            throw new ValidationException("Пользователя с данным id не существует.");
        }
        users.put(user.getId(), user);
    }

    public Collection<User> getUsersList() {
        return users.values();
    }
}
