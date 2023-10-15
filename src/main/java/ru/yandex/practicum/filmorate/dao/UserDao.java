package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface UserDao {

    ArrayList<User> getAllUsers();
    User createUser(User user);
    Optional<User> findUserById(int id);
    User updateUser(User user);

    HashMap<Integer, User> getUserFriends(User user);
    void addFriend(User user, User friend);
    void deleteFriend(User user, User friend);
}
