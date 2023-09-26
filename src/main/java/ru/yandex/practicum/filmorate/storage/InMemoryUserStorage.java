package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int currentId = 0;

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        validateUser(user);

        if (user.getId() == null) {
            user.setId(generateId());
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);

        log.info("Добавлен пользователь: " + user + ". Количество пользователей: " + users.size());
        return user;
    }

    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            log.warn(String.format("Пользователь с данным ID (id = %d) не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с данным ID (id = %d) не найден", id));
        }

        return users.get(id);
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn(String.format("Пользователь с данным ID (id = %d) не найден", user.getId()));
            throw new UserNotFoundException(String.format("Пользователь с данным ID (id = %d) не найден", user.getId()));
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
