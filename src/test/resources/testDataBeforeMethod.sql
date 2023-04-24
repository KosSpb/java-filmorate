INSERT INTO public.users (email,login,user_name,birthday)
    VALUES ('person1@ya.ru', 'person1Login', 'person1Name', '2000-1-1'), 
           ('person2@ya.ru', 'person2Login', 'person2Name', '2001-1-1'),
           ('person3@ya.ru', 'person3Login', 'person3Name', '2002-1-1');
    
INSERT INTO public.friendship (inviter_id,acceptor_id,confirmation_status)
    VALUES (1, 2, false),
           (3, 2, false);
         
INSERT INTO public.film (film_name,description,release_date,duration,mpa_rating_id)
    VALUES ('bestFilm', 'bestDescription', '2022-1-1', 100, 1),
           ('boringFilm', 'boringDescription', '2020-1-1', 70, 2);
   
INSERT INTO public.film_genre (film_id,genre_id)
    VALUES (1, 1);   
          
    
