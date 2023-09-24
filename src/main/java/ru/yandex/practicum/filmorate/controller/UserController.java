package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private UserStorage storage;
    private UserService service;

    @Autowired
    public UserController(UserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return storage.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return storage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return storage.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public ArrayList<User> getFriends(@PathVariable int id) {
        return service.getFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ArrayList<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
