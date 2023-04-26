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

&emsp; SELECT *<br/>
&emsp; FROM film AS f<br/>
&emsp; INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id;<br/>

- получение списка всех жанров:

&emsp;  SELECT *<br/>
&emsp;  FROM genre<br/>

- получение фильма по идентификатору:

&emsp;   SELECT *<br/>
&emsp;   FROM film AS f<br/>
&emsp;   INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id<br/>
&emsp;   WHERE film_id = 1;<br/>

- получение списка из первых n фильмов по количеству лайков:

&emsp;   SELECT *<br/>
&emsp;   FROM film AS f<br/>
&emsp;   INNER JOIN mpa_rating AS mr ON f.mpa_rating_id = mr.mpa_rating_id<br/>
&emsp;   LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id<br/>
&emsp;   GROUP BY f.film_id<br/>
&emsp;   ORDER BY COUNT(l.user_id) DESC<br/>
&emsp;   LIMIT n;<br/>

- получение списка всех пользователей:

&emsp;   SELECT *<br/>
&emsp;   FROM users;<br/>

- получение пользователя по идентификатору:

&emsp;   SELECT *<br/>
&emsp;   FROM users<br/>
&emsp;   WHERE user_id = 1;<br/>

- получение списка друзей:

&emsp;   SELECT *<br/>
&emsp;   FROM users<br/>
&emsp;   WHERE user_id in<br/>
&emsp;   (SELECT acceptor_id<br/>
&emsp;   FROM friendship<br/>
&emsp;   WHERE inviter_id = 1)<br/>