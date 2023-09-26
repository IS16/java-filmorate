package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
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

        if (user1.getFriends().contains(friendId)) {
            throw new AlreadyFriendsException(String.format("Пользователи с id = %d и id = %d уже являются друзьями", userId, friendId));
        }

        user1.addFriend(friendId);
        user2.addFriend(userId);

        log.info("Пользователи " + user1 + " и " + user2 + " стали друзьями");
    }

    public void deleteFriend(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);

        if (!user1.getFriends().contains(friendId)) {
            throw new NotFriendsException(String.format("Пользователи с id = %d и id = %d не являются друзьями", userId, friendId));
        }

        user1.deleteFriend(friendId);
        user2.deleteFriend(userId);

        log.info("Пользователь " + user1 + " удалил из друзей " + user2);
    }

    public ArrayList<User> getFriends(int userId) {
        Set<Integer> friends = storage.getUserById(userId).getFriends();

        ArrayList<User> users = new ArrayList<>();

        friends.forEach(item -> users.add(storage.getUserById(item)));

        return users;
    }

    public ArrayList<User> getCommonFriends(int userId, int friendId) {
        User user1 = storage.getUserById(userId);
        User user2 = storage.getUserById(friendId);

        ArrayList<User> users = new ArrayList<>();

        user1.getFriends().forEach(item -> {
            if (user2.getFriends().contains(item)) {
                users.add(storage.getUserById(item));
            }
        });

        return users;
    }
}
