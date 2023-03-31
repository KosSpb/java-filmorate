package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final ValidationService validationService;
    private final UserRepository userRepository;

    @GetMapping //получение списка всех пользователей
    public Collection<User> getAllUsers() {
        return userRepository.getUsersList();
    }

    @PostMapping //создание пользователя
    public User createUser(@RequestBody User user) {
        validationService.validateUser(user);
        userRepository.createUser(user);
        log.info("createUser: user \"{}\" with id {} was created.", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping //обновление пользователя
    public User updateUser(@RequestBody User user) {
        validationService.validateUser(user);
        userRepository.updateUser(user);
        log.info("updateUser: user \"{}\" with id {} was updated.", user.getLogin(), user.getId());
        return user;
    }
}
