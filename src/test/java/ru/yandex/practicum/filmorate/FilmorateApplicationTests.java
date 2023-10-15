package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RateDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDao userDao;
	private final FilmDao filmDao;
	private final RateDao rateDao;
	private final GenreDao genreDao;

	@Test
	public void testFindUserById() {
		userDao.createUser(new User(0,"sarkisov775@yandex.ru", "sarkisov7745", "", LocalDate.of(2002, 5, 16)));

		Optional<User> userOptional = userDao.findUserById(1);

		assertThat(userOptional)
			.isPresent()
			.hasValueSatisfying(user ->
					assertThat(user).hasFieldOrPropertyWithValue("id", 1)
			);
	}

	@Test
	public void getAllUsersTest() {
		userDao.createUser(new User(0,"sarkisov775@yandex.ru", "sarkisov7745", "", LocalDate.of(2002, 5, 16)));
		userDao.createUser(new User(0, "test@test.ru", "test", "", LocalDate.of(2001, 6, 17)));

		ArrayList<User> users = userDao.getAllUsers();

		assertTrue(users.size() >= 2, "Количество людей не совпадает с требуемым");
	}

	@Test
	public void updateUserTest() {
		userDao.createUser(new User(0,"sarkisov775@yandex.ru", "sarkisov7745", "", LocalDate.of(2002, 5, 16)));

		User user = userDao.findUserById(1).get();

		user.setName("Test name");

		userDao.updateUser(user);

		User user1 = userDao.findUserById(1).get();

		assertEquals("Test name", user1.getName(), "Имя не обновилось");
	}

	@Test
	public void addFriendTest() {
		userDao.createUser(new User(0,"sarkisov775@yandex.ru", "sarkisov7745", "", LocalDate.of(2002, 5, 16)));
		userDao.createUser(new User(0, "test@test.ru", "test", "", LocalDate.of(2001, 6, 17)));

		HashMap<Integer, User> friends = userDao.getUserFriends(userDao.findUserById(1).get());

		assertEquals(0, friends.size(), "Неверное количество друзей. Список должен быть пустым");

		userDao.addFriend(userDao.findUserById(1).get(), userDao.findUserById(2).get());

		HashMap<Integer, User> friendsNew = userDao.getUserFriends(userDao.findUserById(1).get());

		assertEquals(1, friendsNew.size(), "Количество друзей должно увеличиться");

		userDao.deleteFriend(userDao.findUserById(1).get(), userDao.findUserById(2).get());

		HashMap<Integer, User> friends1 = userDao.getUserFriends(userDao.findUserById(1).get());

		assertEquals(0, friends1.size(), "Неверное количество друзей. Список должен быть пустым");
	}

	@Test
	public void testFindFilmById() {
		filmDao.createFilm(new Film(0,"Test", "Test film", LocalDate.of(2000, 1, 1), 250, new Rate(1, "Комедия"), new ArrayList<>()));

		Optional<Film> filmOptional = filmDao.findFilmById(1);

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void getAllFilmsTest() {
		filmDao.createFilm(new Film(0,"Test", "Test film", LocalDate.of(2000, 1, 1), 250, new Rate(1, "Комедия"), new ArrayList<>()));
		filmDao.createFilm(new Film(0,"Test 2", "Test film № 2", LocalDate.of(2001, 1, 1), 430, new Rate(1, "Комедия"), new ArrayList<>()));

		ArrayList<Film> films = filmDao.getAllFilms();

		assertTrue(films.size() >= 2, "Количество фильмов не совпадает с требуемым");
	}

	@Test
	public void updateFilmTest() {
		filmDao.createFilm(new Film(0,"Test 2", "Test film № 2", LocalDate.of(2001, 1, 1), 430, new Rate(1, "Комедия"), new ArrayList<>()));

		Film film = filmDao.findFilmById(1).get();

		film.setReleaseDate(LocalDate.of(2002, 1, 1));

		filmDao.updateFilm(film);

		Film film1 = filmDao.findFilmById(1).get();

		assertEquals(LocalDate.of(2002, 1, 1), film1.getReleaseDate(), "Дата релиза не обновилась");
	}

	@Test
	public void addLikeTest() {
		User user = userDao.createUser(new User(0,"sarkisov775@yandex.ru", "sarkisov7745", "", LocalDate.of(2002, 5, 16)));
		Film film = filmDao.createFilm(new Film(0,"Test 2", "Test film № 2", LocalDate.of(2001, 1, 1), 430, new Rate(1, "Комедия"), new ArrayList<>()));

		ArrayList<Integer> likes = filmDao.getLikesByFilm(film.getId());

		assertEquals(0, likes.size(), "Неверное количество лайков. Список должен быть пустым");

		filmDao.addLike(film.getId(), user.getId());

		ArrayList<Integer> likesNew = filmDao.getLikesByFilm(film.getId());

		assertEquals(1, likesNew.size(), "Количество лайков должно увеличиться");

		filmDao.deleteLike(film.getId(), user.getId());

		ArrayList<Integer> likes1 = filmDao.getLikesByFilm(film.getId());

		assertEquals(0, likes1.size(), "Неверное количество лайков. Список должен быть пустым");
	}

	@Test
	public void mpaTest() {
		Optional<Rate> rate = rateDao.findRateById(1);

		assertThat(rate)
				.isPresent()
				.hasValueSatisfying(item ->
						assertThat(item).hasFieldOrPropertyWithValue("name", "G")
				);

		ArrayList<Rate> rates = rateDao.getAllRates();

		assertEquals(5, rates.size(), "Неверное количество рейтингов");
	}

	@Test
	public void genresTest() {
		Optional<Genre> genre = genreDao.findGenreById(1);

		assertThat(genre)
				.isPresent()
				.hasValueSatisfying(item ->
						assertThat(item).hasFieldOrPropertyWithValue("name", "Комедия")
				);

		ArrayList<Genre> genres = genreDao.getAllGenres();

		assertEquals(6, genres.size(), "Неверное количество жанров");
	}
}