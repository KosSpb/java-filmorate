package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public int addFriend(long id, long friendId) {
        String sqlQuery = "insert into friendship(inviter_id, acceptor_id, confirmation_status) " +
                "values (?, ?, ?)";
        return jdbcTemplate.update(sqlQuery,
                id,
                friendId,
                false);
    }

    @Override
    public int acceptFriendship(long id, long friendId) {
        String sqlQuery = "update friendship set " +
                "confirmation_status = ? " +
                "where inviter_id = ? and acceptor_id = ?";
        return jdbcTemplate.update(sqlQuery,
                true,
                id,
                friendId);
    }

    @Override
    public int removeFriend(long id, long friendId) {
        String sqlQuery = "delete from friendship " +
                "where inviter_id = ? and acceptor_id = ?";
        return jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public Collection<User> getFriendsList(long id) {
        String sqlQuery = "select * from users " +
                "where user_id in " +
                "(select acceptor_id from friendship " +
                "where inviter_id = ?)";
        return jdbcTemplate.query(sqlQuery, userDbStorage::mapRowToUser, id);
    }

    @Override
    public Collection<User> getMutualFriends(long id, long otherId) {
        return getFriendsList(id).stream()
                .filter(getFriendsList(otherId)::contains)
                .collect(Collectors.toList());
    }
}
