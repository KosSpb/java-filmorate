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
