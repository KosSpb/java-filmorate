package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class UserRepository {
    private int generatedId;
    private final Map<Integer, User> users = new HashMap<>();

    public int generateId() {
        return ++generatedId;
    }

    public void createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }
}
