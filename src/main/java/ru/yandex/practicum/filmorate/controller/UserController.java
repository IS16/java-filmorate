package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int currentId = 0;

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }

        validateUser(user);

        users.put(user.getId(), user);

        log.info("Добавлен пользователь: " + user + ". Количество пользователей: " + users.size());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws Exception {
        if (!users.containsKey(user.getId())) {
            log.warn(String.format("Пользователь с данным ID (id = %d) не найден", user.getId()));
            throw new EntityNotFoundException(String.format("Пользователь с данным ID (id = %d) не найден", user.getId()));
        }

        validateUser(user);

        users.put(user.getId(), user);

        log.info("Обновлён пользователь: " + user);
        return user;
    }

    private int generateId() {
        return ++currentId;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
