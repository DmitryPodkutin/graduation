# Voting system for deciding where to have lunch.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8349247a1800468ab3a50526f5b31ecb)](https://app.codacy.com/gh/DmitryPodkutin/graduation?utm_source=github.com&utm_medium=referral&utm_content=DmitryPodkutin/graduation&utm_campaign=Badge_Grade)

**REST API using Hibernate/Spring/SpringMVC (or Spring-Boot)**

### Description

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
------
##  Commands for Using application
curl samples (application deployed at application context graduation).
For windows use Git Bash


### - **user** -


**get All Restaurants** (RequestParam - withMenu=false/true , default withMenu=true)

```sh
curl -s http://localhost:8080/graduation/profile/restaurants/?withMenu=false --user user@yandex.ru:password
```
```json
RESULT

[
    {
        "id": 100003,
        "name": "Colonies"
    },
    {
        "id": 100002,
        "name": "Debasus"
    },
    {
        "id": 100004,
        "name": "The Lounge Cafe"
    }
]
```

**get Restaurant** (RequestParam - withMenu=false/true , default withMenu=true)

```sh
curl -s http://localhost:8080/graduation/profile/restaurants/100002?\withMenu\=false  --user user@yandex.ru:password
```

```json
RESULT

{
        "id": 100002,
        "menu": [
            {
                "date": "2020-12-22",
                "id": 100005,
                "name": "Bear",
                "price": 120
            },
            {
                "date": "2020-12-22",
                "id": 100006,
                "name": "Garlic bread",
                "price": 670
            },
            {
                "date": "2020-12-22",
                "id": 100007,
                "name": "BBQ ribs",
                "price": 340
            }
        ],
        "name": "Debasus"
    }
```

**get Vote** (vote to the current date)
```sh
curl -s http://localhost:8080/graduation/profile/votes  --user user@yandex.ru:password
```
**create Vote**  
```sh
curl -s -X POST http://localhost:8080/graduation/profile/restaurants/100003/votes  --user user@yandex.ru:password
```
**update Vote** (you can only change your voice until 11:00AM)
```sh
curl -s -X PUT http://localhost:8080/graduation/profile/restaurants/100004/votes/100019  --user user@yandex.ru:password
```

### - **admin** -

**get All Restaurants** (RequestParam - withMenu=false/true , default withMenu=true)
```sh
curl -s http://localhost:8080/graduation/profile/restaurants  --user admin@gmail.com:admin
```
**create Restaurant**
```sh
curl -s -X POST -d '{"name":"NewRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants --user admin@gmail.com:admin
```

**create new Dish**
```sh
curl -s -X POST -d '{"date":"2020-12-21","name":"BigMac ","price":777}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/graduation/admin/restaurants/100003/dishes --user admin@gmail.com:admin
```

**get All Dishes For Restaurant** (order by date)
```sh
curl -s http://localhost:8080/graduation/admin/restaurants/100003/dishes --user admin@gmail.com:admin
```
**delete Dish**
```sh
curl -s -i -X DELETE http://localhost:8080/graduation/admin/restaurants/100003/dishes/100010  --user admin@gmail.com:admin
```
