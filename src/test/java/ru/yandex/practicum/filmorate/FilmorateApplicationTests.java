package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void userWithInvalidEmailTest() throws IOException, InterruptedException {
		String json = "{\"email\":\"sarkisov775\",\"login\":\"IS\",\"name\":\"Ilya\",\"birthday\":\"2002-05-16\"}";

		URI url = URI.create("http://localhost:8080/users");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();


		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял невалидную почту");
	}

	@Test
	void userWithEmptyLoginTest() throws IOException, InterruptedException {
		String json = "{\"email\":\"sarkisov775@ya.ru\",\"login\":\"\",\"birthday\":\"2002-05-16\"}";

		URI url = URI.create("http://localhost:8080/users");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял пустой логин");
	}

	@Test
	void userLoginHasSpaceTest() throws IOException, InterruptedException {
		String json = "{\"email\":\"sarkisov775@ya.ru\",\"login\":\"Ааа ббб\",\"birthday\":\"2002-05-16\"}";

		URI url = URI.create("http://localhost:8080/users");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(500, response.statusCode(), "Эндпоинт принял логин c пробелом");
	}

	@Test
	void userShouldHaveNameFromLoginTest() throws IOException, InterruptedException, JSONException {
		String json = "{\"email\":\"sarkisov775@ya.ru\",\"login\":\"IS\",\"birthday\":\"2002-05-16\"}";

		URI url = URI.create("http://localhost:8080/users");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode(), "Эндпоинт не принял запрос с пустым именем");
		System.out.println(response.body());
		assertEquals("IS", new JSONObject(response.body()).get("name"), "Логин не установился в качестве имени");
	}

	@Test
	void userWithInvalidBirthdayTest() throws IOException, InterruptedException {
		String json = "{\"email\":\"sarkisov775@ya.ru\",\"login\":\"IS\",\"birthday\":\"2040-05-16\"}";

		URI url = URI.create("http://localhost:8080/users");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял дату в будущем");
	}

	@Test
	void filmWithEmptyNameTest() throws IOException, InterruptedException {
		String json = "{\"name\":\"\",\"description\":\"Тестовый фильм\",\"releaseDate\":\"2040-05-16\",\"duration\":5}";

		URI url = URI.create("http://localhost:8080/films");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял фильм без названия");
	}

	@Test
	void filmWithDescriptionMoreThan200SymbolsTest() throws IOException, InterruptedException {
		String json = "{\"name\":\"Тест\",\"description\":\"Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12Тестовый12\",\"releaseDate\":\"2040-05-16\",\"duration\":5}";

		URI url = URI.create("http://localhost:8080/films");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял фильм с описанием более 200 символов");
	}

	@Test
	void filmWithZeroDurationTest() throws IOException, InterruptedException {
		String json = "{\"name\":\"Тест\",\"description\":\"Тест\",\"releaseDate\":\"2040-05-16\",\"duration\":0}";

		URI url = URI.create("http://localhost:8080/films");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode(), "Эндпоинт принял фильм с длительностью 0");
	}

	@Test
	void filmWithInvalidReleaseDateTest() throws IOException, InterruptedException {
		String json = "{\"name\":\"Тест\",\"description\":\"Тест\",\"releaseDate\":\"1234-05-16\",\"duration\":1}";

		URI url = URI.create("http://localhost:8080/films");
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).setHeader("content-type", "application/json").build();
		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(500, response.statusCode(), "Эндпоинт принял фильм c датой релиза ранее 28.12.1895");
	}
}
