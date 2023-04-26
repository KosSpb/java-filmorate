package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class FriendshipService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Autowired
    public FriendshipService(FriendshipStorage friendshipStorage, UserStorage userStorage) {
        this.friendshipStorage = friendshipStorage;
        this.userStorage = userStorage;
    }

    public User addFriend(long id, long friendId) {
        User user = userStorage.getUserById(id).orElseThrow(() -> {
            log.info("addFriend - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        });

        if (userStorage.getUserById(friendId).isEmpty()) {
            log.info("addFriend - friend id not found: {}", friendId);
            throw new NotFoundException("Друга с данным id не существует.");
        }

        if (friendshipStorage.addFriend(id, friendId) != 0) {
            log.info("addFriend: user with id {} added friend with id {}.", id, friendId);
        } else {
            log.info("addFriend - friend id already exists: {}", friendId);
            throw new AlreadyExistException("Друг с данным id уже добавлен.");
        }

        return user;
    }

    public User acceptFriendship(long id, long friendId) {
        User user = userStorage.getUserById(id).orElseThrow(() -> {
            log.info("acceptFriendship - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        });

        if (userStorage.getUserById(friendId).isEmpty()) {
            log.info("acceptFriendship - friend id not found: {}", friendId);
            throw new NotFoundException("Друга с данным id не существует.");
        }

        if (friendshipStorage.acceptFriendship(id, friendId) != 0) {
            log.info("acceptFriendship: friend with id {} accepted friendship with user with id {}.", friendId, id);
        } else {
            log.info("acceptFriendship - user with id {} is not friend of user with id {}.", id, friendId);
            throw new AlreadyExistException("Данные пользователи не являются друзьями.");
        }

        return user;
    }

    public User removeFriend(long id, long friendId) {
        User user = userStorage.getUserById(id).orElseThrow(() -> {
            log.info("removeFriend - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        });

        if (userStorage.getUserById(friendId).isEmpty()) {
            log.info("removeFriend - friend id not found: {}", friendId);
            throw new NotFoundException("Друга с данным id не существует.");
        }

        if (friendshipStorage.removeFriend(id, friendId) != 0) {
            log.info("removeFriend: user with id {} removed friend with id {}.", id, friendId);
        } else {
            log.info("removeFriend - friend id was already removed: {}", friendId);
            throw new NotFoundException("Друг с данным id отсутствует в списке друзей.");
        }

        return user;
    }

    public Collection<User> getFriendsList(long id) {
        if (userStorage.getUserById(id).isPresent()) {
            return friendshipStorage.getFriendsList(id);
        } else {
            log.info("getFriendsList - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }
    }

    public Collection<User> getMutualFriends(long id, long otherId) {
        if (userStorage.getUserById(id).isEmpty()) {
            log.info("getMutualFriends - user id not found: {}", id);
            throw new NotFoundException("Пользователя с данным id не существует.");
        }
        if (userStorage.getUserById(otherId).isEmpty()) {
            log.info("getMutualFriends - other user id not found: {}", otherId);
            throw new NotFoundException("Другого пользователя с данным id не существует.");
        }

        return friendshipStorage.getMutualFriends(id, otherId);
    }
}
