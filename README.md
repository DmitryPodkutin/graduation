[![Codacy Badge](https://app.codacy.com/project/badge/Grade/a4b79bfd96ce4a0e96b145af99eaf95b)](https://www.codacy.com/gh/DmitryPodkutin/voting_system/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=DmitryPodkutin/voting_system&amp;utm_campaign=Badge_Grade)
# Voting system for deciding where to have lunch.

**REST API using Maven/Hibernate/Spring/SpringMVC/Security?REST(Jackson),  
Java 8 Stream and Time API Storage in databases HSQLDB, Swagger**

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
#### REST UI Documentation
The application has Swagger UI.  
After starting APP you need to go about url : http://localhost:8080/voting-system/  
<ins><b>Login/Password for testing:</b></ins>  
<b>ADMIN: </b> admin@gmail.com / admin  
<b>USER: </b> user@yandex.ru / password  


## Commands for Using application
curl samples (application deployed at application context voting-system).
For windows use Git Bash


<b><h3><ins>- user -</ins></h3></b>

**get All Restaurants** (RequestParam - withMenu=false/true , default withMenu=true)

```sh
curl -s http://localhost:8080/voting-system/profile/restaurants/ --user user@yandex.ru:password
```
<b>
<details>
<summary><b>RESULT</b></summary>
<pre>

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
</pre>
</details>
</b>
<br>

**get Restaurant** (RequestParam - withMenu=false/true , default withMenu=true)

```sh
curl -s http://localhost:8080/voting-system/profile/restaurants/100002  --user user@yandex.ru:password
```
<b>
<details>
<summary><b>RESULT</b></summary>
<pre>

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
</pre>
</details>
</b>
<br>

**get Vote** (vote to the current date)
```sh
curl -s http://localhost:8080/voting-system/profile/votes  --user user@yandex.ru:password
```
**create Vote**  
```sh
curl -s -X POST http://localhost:8080/voting-system/profile/restaurants/100003/votes  --user user@yandex.ru:password
```
**update Vote** (you can only change your voice until 11:00AM)
```sh
curl -s -X PUT http://localhost:8080/voting-system/profile/restaurants/100004/votes/100019  --user user@yandex.ru:password
```

<b><h3><ins>- admin -</ins></h3></b>

**get All Restaurants** (RequestParam - withMenu=false/true , default withMenu=true)
```sh
curl -s http://localhost:8080/voting-system/profile/restaurants  --user admin@gmail.com:admin
```
**create Restaurant**
```sh
curl -s -X POST -d '{"name":"NewRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting-system/admin/restaurants --user admin@gmail.com:admin
```

**create new Dish**
```sh
curl -s -X POST -d '{"date":"2020-12-21","name":"BigMac ","price":777}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting-system/admin/restaurants/100003/dishes --user admin@gmail.com:admin
```

**get All Dishes For Restaurant** (order by date)
```sh
curl -s http://localhost:8080/voting-system/admin/restaurants/100003/dishes --user admin@gmail.com:admin
```
**delete Dish**
```sh
curl -s -i -X DELETE http://localhost:8080/voting-system/admin/restaurants/100003/dishes/100010  --user admin@gmail.com:admin
```

**Get a history of restaurant Votes**

```sh
curl -s  http://localhost:8080/voting-system/admin/restaurants/100004/votes --user admin@gmail.com:admin
```

<b>
<details>
<summary><b>RESULT</b></summary>
<pre>

{
      "date": "2020-10-19",
      "id": 100015,
      "restaurant": {
          "id": 100004,
          "name": "The Lounge Cafe"
      }
  },
  {
      "date": "2020-10-23",
      "id": 100014,
      "restaurant": {
          "id": 100004,
          "name": "The Lounge Cafe"
      }
  },
  {
      "date": "2020-11-09",
      "id": 100017,
      "restaurant": {
          "id": 100004,
          "name": "The Lounge Cafe"
      }
  }
</pre>
</details>
</b>
<br>
