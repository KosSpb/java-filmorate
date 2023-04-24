package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {
    int addFriend(long id, long friendId);

    int acceptFriendship(long id, long friendId);

    int removeFriend(long id, long friendId);

    Collection<User> getFriendsList(long id);

    Collection<User> getMutualFriends(long id, long otherId);
}
