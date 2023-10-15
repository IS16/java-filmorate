package ru.yandex.practicum.filmorate.storage.Users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    public ArrayList<User> getAllUsers();

    public User getUserById(int id);

    public User createUser(User user);

    public User updateUser(User user);

    public boolean isFriend(User user, User friend);
    public void addFriend(User user, User friend);
    public void deleteFriend(User user, User friend);
    public HashMap<Integer, User> getFriends(User user);
}
