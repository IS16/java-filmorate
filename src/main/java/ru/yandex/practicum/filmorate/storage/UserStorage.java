package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    int currentId = 0;
    HashMap<Integer, User> users = null;

    public ArrayList<User> getAllUsers();
    public User getUserById(int id);
    public User createUser(User user);
    public User updateUser(User user);
}
