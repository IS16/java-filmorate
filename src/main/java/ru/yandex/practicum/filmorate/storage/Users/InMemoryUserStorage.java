package ru.yandex.practicum.filmorate.storage.Users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int currentId = 0;

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        validateUser(user);

        if (user.getId() == 0) {
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

    @Override
    public boolean isFriend(User user, User friend) {
        return user.getFriends().contains(friend.getId());
    }

    @Override
    public void addFriend(User user, User friend) {
        log.info(String.format("Пользователь (id = %d) добавил в друзья пользователя (id = %d)", user.getId(), friend.getId()));
        user.addFriend(friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        log.info(String.format("Пользователь (id = %d) удалил из друзей пользователя (id = %d)", user.getId(), friend.getId()));
        user.deleteFriend(friend.getId());
    }

    @Override
    public HashMap<Integer, User> getFriends(User user) {
        Set<Integer> friends = user.getFriends();

        HashMap<Integer, User> users = new HashMap<>();

        friends.forEach(item -> users.put(item, getUserById(item)));

        return users;
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
