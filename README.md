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

&emsp; SELECT f.film_id,<br/>
&emsp;        f.film_name,<br/>
&emsp;        f.description,<br/>
&emsp;        f.release_date,<br/>
&emsp;        f.duration,<br/>
&emsp;        mr.value<br/>
&emsp; FROM film AS f<br/>
&emsp; INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id;<br/>

- получение списка всех жанров всех фильмов:

&emsp;  SELECT f.film_id,<br/>
&emsp;         f.film_name,<br/>
&emsp;         g.genre_name<br/>
&emsp;  FROM film_genre AS fg<br/>
&emsp;  INNER JOIN genre AS g ON fg.genre_id = g.genre_id<br/>
&emsp;  INNER JOIN film AS f ON fg.film_id = f.film_id;<br/>

- получение фильма по идентификатору:

&emsp;   SELECT f.film_id,<br/>
&emsp;          f.film_name,<br/>
&emsp;          f.description,<br/>
&emsp;          f.release_date,<br/>
&emsp;          f.duration,<br/>
&emsp;          mr.value<br/>
&emsp;   FROM film AS f<br/>
&emsp;   INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id<br/>
&emsp;   WHERE film_id = 1;<br/>

- получение списка из первых n фильмов по количеству лайков:

&emsp;   SELECT f.film_name,<br/>
&emsp;          f.description,<br/>
&emsp;          f.release_date,<br/>
&emsp;          f.duration,<br/>
&emsp;          mr.value<br/>
&emsp;   FROM<br/>
&emsp;       (SELECT film_id,<br/>
&emsp;               COUNT(user_id) AS total_likes<br/>
&emsp;          FROM likes<br/>
&emsp;        GROUP BY film_id<br/>
&emsp;        ORDER BY total_likes DESC) AS l<br/>
&emsp;   LEFT OUTER JOIN film AS f ON l.film_id = f.film_id<br/>
&emsp;   LEFT OUTER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id<br/>
&emsp;   LIMIT n;<br/>

- получение списка всех пользователей:

&emsp;   SELECT *<br/>
&emsp;   FROM user;<br/>

- получение пользователя по идентификатору:

&emsp;   SELECT *<br/>
&emsp;   FROM user<br/>
&emsp;   WHERE user_id = 1;<br/>

- получение списка друзей:

&emsp;   SELECT u.user_id,<br/>
&emsp;          u.email,<br/>
&emsp;          u.login,<br/>
&emsp;          u.user_name,<br/>
&emsp;          u.birthday<br/>
&emsp;   FROM<br/>
&emsp;      (SELECT acceptor_id<br/>
&emsp;         FROM friendship<br/>
&emsp;        WHERE inviter_id = 1<br/>
&emsp;          AND confirmation_status = 1) AS fr<br/>
&emsp;   INNER JOIN user AS u ON fr.acceptor_id = u.user_id;<br/>

- получение списка друзей, общих с другим пользователем:

&emsp;   SELECT u.user_id,<br/>
&emsp;          u.email,<br/>
&emsp;          u.login,<br/>
&emsp;          u.user_name,<br/>
&emsp;          u.birthday<br/>
&emsp;   FROM<br/>
&emsp;      (SELECT acceptor_id<br/>
&emsp;         FROM friendship<br/>
&emsp;        WHERE inviter_id = 1<br/>
&emsp;          AND confirmation_status = 1) AS fr<br/>
&emsp;   INNER JOIN<br/>
&emsp;      (SELECT acceptor_id<br/>
&emsp;         FROM friendship<br/>
&emsp;        WHERE inviter_id = 2<br/>
&emsp;          AND confirmation_status = 1) AS other_fr ON fr.acceptor_id = other_fr.acceptor_id<br/>
&emsp;   INNER JOIN user AS u ON other_fr.acceptor_id = u.user_id;<br/>