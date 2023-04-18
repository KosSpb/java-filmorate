# java-filmorate

ER-диаграмма:

![alt text](https://github.com/KosSpb/java-filmorate/blob/er-diagram/erDiagram.png?raw=true)
_______________________________________________________________
film:
Содержит данные о фильмах.

Таблица включает поля:
- первичный ключ film_id - идентификатор фильма;
- film_name - название фильма;
- description - описание фильма;
- release_date - дата релиза;
- duration - продолжительность фильма;
- mpa_rating_id - идентификатор рейтинга фильма по Motion Picture Association;
_______________________________________________________________
film_genre:
Связующая таблица для фильмов и жанров.

Таблица включает поля:
- первичный ключ film_id - идентификатор фильма;
- первичный ключ genre_id - идентификатор жанра;
_______________________________________________________________
friendship:
Содержит сведения о друзьях пользователей.

Таблица включает поля:
- первичный ключ inviter_id - идентификатор пользователя, который отправил запрос о добавлении в друзья;
- первичный ключ acceptor_id - идентификатор пользователя, кому был отправлен запрос о добавлении в друзья;
- confirmation_status - статус подтверждения направленного запроса о добавлении в друзья;
_______________________________________________________________
genre:
Содержит перечень жанров фильмов.

Таблица включает поля:
- первичный ключ genre_id - идентификатор жанра;
- genre_name - название жанра;
_______________________________________________________________
likes:
Содержит сведения о лайках, которые пользователи поставили фильмам.

Таблица включает поля:
- первичный ключ film_id - идентификатор фильма;
- первичный ключ user_id - идентификатор пользователя;
_______________________________________________________________
mpa_rating:
Содержит перечень рейтингов Motion Picture Association.

Таблица включает поля:
- первичный ключ mpa_rating_id - идентификатор рейтинга фильма по Motion Picture Association;
- value - значение рейтинга;
_______________________________________________________________
user:
Содержит данные о пользователях.

Таблица включает поля:
- первичный ключ user_id - идентификатор пользователя;
- email - электронная почта пользователя;
- login - логин пользователя;
- user_name - имя для отображения;
- birthday - дата рождения;
_______________________________________________________________
Примеры запросов для основных операций:

- получение списка всех фильмов:

  SELECT f.film_id, 
         f.film_name,
         f.description,
         f.release_date,
         f.duration,
         mr.value
  FROM film AS f
  INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id;

- получение списка всех жанров всех фильмов:

  SELECT f.film_id,
         f.film_name,
         g.genre_name
  FROM film_genre AS fg
  INNER JOIN genre AS g ON fg.genre_id = g.genre_id
  INNER JOIN film AS f ON fg.film_id = f.film_id;

- получение фильма по идентификатору:

  SELECT f.film_id,
         f.film_name,
         f.description,
         f.release_date,
         f.duration,
         mr.value
  FROM film AS f
  INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id
  WHERE film_id = 1;

- получение списка из первых n фильмов по количеству лайков:

  SELECT f.film_name,
         f.description,
         f.release_date,
         f.duration,
         mr.value
  FROM
      (SELECT film_id,
              COUNT(user_id) AS total_likes
         FROM likes
       GROUP BY film_id
       ORDER BY total_likes DESC) AS l
  LEFT OUTER JOIN film AS f ON l.film_id = f.film_id
  LEFT OUTER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id
  LIMIT n;

- получение списка всех пользователей:

  SELECT *
  FROM user;

- получение пользователя по идентификатору:

  SELECT *
  FROM user
  WHERE user_id = 1;

- получение списка друзей:

  SELECT u.user_id,
         u.email,
         u.login,
         u.user_name,
         u.birthday
  FROM
     (SELECT acceptor_id
        FROM friendship
       WHERE inviter_id = 1
         AND confirmation_status = 1) AS fr
  INNER JOIN user AS u ON fr.acceptor_id = u.user_id;

- получение списка друзей, общих с другим пользователем:

  SELECT u.user_id,
         u.email,
         u.login,
         u.user_name,
         u.birthday
  FROM
     (SELECT acceptor_id
        FROM friendship
       WHERE inviter_id = 1
         AND confirmation_status = 1) AS fr
  INNER JOIN
     (SELECT acceptor_id
        FROM friendship
       WHERE inviter_id = 2
         AND confirmation_status = 1) AS other_fr ON fr.acceptor_id = other_fr.acceptor_id
  INNER JOIN user AS u ON other_fr.acceptor_id = u.user_id;