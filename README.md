# Сервис авторизации и проведения платежей

**API** с возможностью авторизации пользователя и 
совершения платежа после успешной авторизации.
</br></br>
**3 основных endpoint'а:** 
- **login** - при успешном вводе логина и пароля выдает токен;
- **logout** - делает токен недействительным;
- **payment** - осуществление платежа.
<br>
  
При добавлении пользователя в БД, баланс устанавливается равным
8 USD, сама операция "зфньуте" позволяет снимать с баланса
пользователя 1.1 USD при каждом вызове, все 
совершенные платежи хранятся в БД.

# Stack

![](https://img.shields.io/badge/java-✓-blue.svg)
![](https://img.shields.io/badge/spring_boot-✓-blue.svg)
![](https://img.shields.io/badge/postgresql-✓-blue.svg)
![](https://img.shields.io/badge/jwt-✓-blue.svg)

> При тестировании, для запросов, рекомендуется использование **Postman**

Пример запроса на регистрацию пользователя:

- **post:** .../register
- Тело запроса:

```json
{
  "login": "user",
  "password": "user",
  "role": "user",
  "firstName": "Ivan",
  "lastName": "Ivanov"
}
```
Пример запроса на авторизацию пользователя:

- **post:** .../login
- Тело запроса:

```json
{
  "login": "user",
  "password": "user"
}
```

Пример ответа:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjE4MDAyMDAwfQ.yYygq9GuE43k8gXAmpxCXkbsvu2dlZhR0mF27h_O24u-9wgt6tbnGDtTphsb7bobu84GpE6UzFoPkP6xHz4dqQ"
}
```

Пример авторизованного запроса:

- **get:** .../user/test
- Header запроса должен содержать ключ - **Authorization** и полученный при авторизации **token**

Пример запроса на выход пользователя с **"login" = "user"** из системы:

- **post:** .../user/user/logout
- Header запроса должен содержать ключ - **Authorization** и полученный при авторизации **token**