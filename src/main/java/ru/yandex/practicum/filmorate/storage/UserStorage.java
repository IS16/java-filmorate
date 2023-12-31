package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    public ArrayList<User> getAllUsers();

    public User getUserById(int id);

    public User createUser(User user);

    public User updateUser(User user);
}
