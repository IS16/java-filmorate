package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Users.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class UserService {
    private UserStorage storage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public ArrayList<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUserById(int id) {
        return storage.getUserById(id);
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);

        if (storage.isFriend(user1, user2)) {
            throw new AlreadyFriendsException(String.format("Пользователи с id = %d и id = %d уже являются друзьями", userId, friendId));
        }

        storage.addFriend(user1, user2);

        log.info("Пользователи " + user1 + " и " + user2 + " стали друзьями");
    }

    public void deleteFriend(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);

        if (!storage.isFriend(user1, user2)) {
            throw new NotFriendsException(String.format("Пользователи с id = %d и id = %d не являются друзьями", userId, friendId));
        }

        storage.deleteFriend(user1, user2);

        log.info("Пользователь " + user1 + " удалил из друзей " + user2);
    }

    public ArrayList<User> getFriends(int userId) {
        return new ArrayList<>(storage.getFriends(getUserById(userId)).values());
    }

    public ArrayList<User> getCommonFriends(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);

        HashMap<Integer, User> user1Friends = storage.getFriends(user1);
        HashMap<Integer, User> user2Friends = storage.getFriends(user2);

        ArrayList<User> users = new ArrayList<>();

        storage.getFriends(user1).keySet().forEach(item -> {
            if (user2Friends.containsKey(item)) {
                users.add(user1Friends.get(item));
            }
        });

        return users;
    }
}
