package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pr = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, user.getEmail());
            pr.setString(2, user.getLogin());
            pr.setString(3, user.getName());
            pr.setDate(4, Date.valueOf(user.getBirthday()));
            return pr;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public Optional<User> findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);

        if (userRows.next()) {
            User user = new User(
                userRows.getInt("id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("name"),
                userRows.getDate("birthday").toLocalDate()
            );
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<User> getAllUsers() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        ArrayList<User> users = new ArrayList<>();

        while (userRows.next()) {
            users.add(new User(
                userRows.getInt("id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("name"),
                userRows.getDate("birthday").toLocalDate()
            ));
        }

        return users;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return user;
    }

    @Override
    public HashMap<Integer, User> getUserFriends(User user) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT * FROM friendship WHERE first_user_id = ?", user.getId());

        HashMap<Integer, User> friends = new HashMap<>();

        while (friendsRows.next()) {
            friends.put(friendsRows.getInt("second_user_id"), findUserById(friendsRows.getInt("second_user_id")).get());
        }

        return friends;
    }

    @Override
    public void addFriend(User user, User friend) {
        String sqlQuery = "INSERT INTO friendship (first_user_id, second_user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                friend.getId()
        );
    }

    @Override
    public void deleteFriend(User user, User friend) {
        String sqlQuery = "DELETE FROM friendship WHERE first_user_id = ? AND second_user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }
}
