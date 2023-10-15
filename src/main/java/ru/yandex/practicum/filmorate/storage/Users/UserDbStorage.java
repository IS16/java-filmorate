package ru.yandex.practicum.filmorate.storage.Users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final UserDao userDao;

    public UserDbStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    public ArrayList<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(int id) {
        Optional<User> user = userDao.findUserById(id);
        if (user.isEmpty()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь с данным ID (id = %d) не найден", id));
        }

        log.info("Найден пользователь: {} {}", user.get().getId(), user.get().getLogin());
        return user.get();
    }

    public User createUser(User user) {
        validateUser(user);

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User createdUser = userDao.createUser(user);
        log.info("Добавлен пользователь: " + createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        validateUser(user);

        return userDao.updateUser(user);
    }

    public void addFriend(User user, User friend) {
        userDao.addFriend(user, friend);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        userDao.deleteFriend(user, friend);
    }

    public boolean isFriend(User user, User friend) {
        HashMap<Integer, User> friends = userDao.getUserFriends(user);

        return friends.containsKey(friend.getId());
    }

    public HashMap<Integer, User> getFriends(User user) {
        return userDao.getUserFriends(user);
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
