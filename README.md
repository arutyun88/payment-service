# Сервис авторизации и проведения платежей

# Stack

![](https://img.shields.io/badge/java_8-✓-blue.svg)
![](https://img.shields.io/badge/spring_boot-✓-blue.svg)
![](https://img.shields.io/badge/postgresql-✓-blue.svg)
![](https://img.shields.io/badge/jwt-✓-blue.svg)

> При тестировании, для запрососв, рекомендуется использование **Postman**

Пример запроса на регистрацию пользователя:

- **post:** .../register
- При регистрацию пользователю автоматически назначается роль **USER**
- Тело запроса:

```json
{
  "login": "user",
  "password": "user"
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

- **post:** .../user/get
- Header запроса должен содержать ключ - **Authorization** и полученный при авторизации **token**

