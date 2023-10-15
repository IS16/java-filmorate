CREATE TABLE IF NOT EXISTS films (
  id integer UNIQUE PRIMARY KEY AUTO_INCREMENT,
  name varchar,
  description varchar(200),
  releaseDate datetime,
  duration int,
  rate_id integer
);

CREATE TABLE IF NOT EXISTS users (
  id integer UNIQUE PRIMARY KEY AUTO_INCREMENT,
  email varchar,
  login varchar,
  name varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS likes (
  film_id integer,
  user_id integer
);

CREATE TABLE IF NOT EXISTS genres (
  id integer UNIQUE PRIMARY KEY AUTO_INCREMENT,
  name varchar
);

CREATE TABLE IF NOT EXISTS rates (
  id integer UNIQUE PRIMARY KEY AUTO_INCREMENT,
  name varchar
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id integer,
  genre_id integer
);

CREATE TABLE IF NOT EXISTS friendship (
  first_user_id integer,
  second_user_id integer
);

ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres (id);

ALTER TABLE friendship ADD FOREIGN KEY (first_user_id) REFERENCES users (id);

ALTER TABLE friendship ADD FOREIGN KEY (second_user_id) REFERENCES users (id);

ALTER TABLE films ADD FOREIGN KEY (rate_id) REFERENCES rates (id);