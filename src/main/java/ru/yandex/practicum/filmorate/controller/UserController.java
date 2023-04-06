package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ValidationService validationService;
    private final UserService userService;

    @Autowired
    public UserController(ValidationService validationService, UserService userService) {
        this.validationService = validationService;
        this.userService = userService;
    }

    @GetMapping //получение списка всех пользователей
    public Collection<User> getAllUsers() {
        return userService.getUsersList();
    }

    @PostMapping //создание пользователя
    public User createUser(@RequestBody User user) {
        validationService.validateUser(user);
        userService.createUser(user);
        return user;
    }

    @PutMapping //обновление пользователя
    public User updateUser(@RequestBody User user) {
        validationService.validateUser(user);
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}") //получение пользователя по идентификатору
    public User getUserById(@PathVariable(value = "id") int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление в друзья
    public User addFriend(@PathVariable(value = "id") int id,
                          @PathVariable(value = "friendId") int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") //удаление из друзей
    public User removeFriend(@PathVariable(value = "id") int id,
                             @PathVariable(value = "friendId") int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends") //возвращаем список пользователей, являющихся его друзьями
    public Collection<User> getFriendsList(@PathVariable(value = "id") int id) {
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") //возвращаем список друзей, общих с другим пользователем
    public Collection<User> getMutualFriends(@PathVariable(value = "id") int id,
                                             @PathVariable(value = "otherId") int otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
