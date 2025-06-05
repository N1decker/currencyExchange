# Проект “Обмен валют”

Проект “Обмен валют” представляет собой REST API для описания валют и обменных курсов.
### Описание методов REST API
Описание методов REST API и техническое задание представлены по ссылке - https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/

### Используемые технологии и библиотеки:
- Java 22
- JDBC, HikariCP
- Java Servlets
- SQLite
- MapStruct
- В качестве сервера для запуска используется Apache Tomcat 9
- В качестве фронтенда используется: https://github.com/zhukovsd/currency-exchange-frontend

### Сборка и запуск проекта на локальном сервере
Для сборки проекта используется Maven и MavenWrapper

База данных SQLite в проекте уже создана и в нее добавлены таблицы с несколькими значениями.

Так же для удобства в папке resources/db/ лежат sql скрипты для создания и заполнения базы данных.

### Расхождения в api
Запросы принимаются с /api/(/api/currencies, /api/exchangeRates etc.)