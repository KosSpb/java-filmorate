package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

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
        log.info("createUser: user \"{}\" with id {} was created.", user.getLogin(), user.getId());
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
        log.info("updateUser: user \"{}\" with id {} was updated.", user.getLogin(), user.getId());
    }

    public Collection<User> getUsersList() {
        return userStorage.getUsersList();
    }

    public User getUserById(int id) {
       return userStorage.getUserById(id);
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        if (!userStorage.getUsersList().contains(userStorage.getUserById(friendId))) {
            log.info("addFriend - friend id not found: {}", friendId);
            throw new NotFoundException("Друга с данным id не существует.");
        }

        if (user.getFriends().add(friendId)) {
            userStorage.getUserById(friendId).getFriends().add(id);
            log.info("addFriend: user with id {} added friend with id {}.", id, friendId);
        } else {
            log.info("addFriend - friend id already exists: {}", friendId);
            throw new AlreadyExistException("Друг с данным id уже добавлен.");
        }
        return user;
    }

    public User removeFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        if (!userStorage.getUsersList().contains(userStorage.getUserById(friendId))) {
            log.info("removeFriend - friend id not found: {}", friendId);
            throw new NotFoundException("Друга с данным id не существует.");
        }

        if (user.getFriends().remove(friendId)) {
            userStorage.getUserById(friendId).getFriends().remove(id);
            log.info("removeFriend: user with id {} removed friend with id {}.", id, friendId);
        } else {
            log.info("removeFriend - friend id was already removed: {}", friendId);
            throw new NotFoundException("Друг с данным id отсутствует в списке друзей.");
        }
        return user;
    }

    public Collection<User> getFriendsList(int id) {
        User user = userStorage.getUserById(id);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(int id, int otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
