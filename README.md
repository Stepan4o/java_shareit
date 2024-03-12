# ShareIt  
### Приложение на SpringBoot для шеринга вещей между пользователями  

Пользователи приложения имеют возможность добавлять вещи, которыми они могли бы поделиться, бронировать вещи других пользователей, оставлять комментарии к вещи после успешного завершения аренды, осуществлять поиск по совпадениям в названии/описании вещи. В роли владельца можно подтверждать/отклонять запрос на бронирование, а также просматривать свои (ближайшее/последнее) бронирования. Также пользователи могут создавать запросы с кратким описанием вещи, которую они хотели бы арендовать. В свою очередь, другие пользователи могут отвечать на подобные запросы, прилагая свои вещи, подходящие по описанию к данным запросам.  

### Учебный проект был реализован на курсе Java-developer от YandexPracticum в течение четырёх спринтов:  
**Стек:** Java11, SpringBoot, Maven, Hibernate, REST, PostgreSQL, H2, JPA, Postman, Docker, JUnit5, Mockito, Lombok
1. Ветка [add-controllers](https://github.com/Stepan4o/java_shareit/tree/add-controllers) : Создание каркаса приложения и части его веб слоя, разработка моделей данных user и item, создание DTO объектов, а так же маппер-классов. Хранение данных в оперативной памяти приложения;
2. Ветка [add-bookings](https://github.com/Stepan4o/java_shareit/tree/add-bookings) : Реализвация модели данных booking и comment. Функциональность бронирования вещи, а так же добавление возможности оставить комментарий к вещи после успешного завершения аренды. Работа с Hibernate, подключение H2 для тестов, хранение в базе данных;
3. Ветка [add-item-requests](https://github.com/Stepan4o/java_shareit/tree/add-item-requests) : Добавление функциональности создания запросов на желаемую вещь с её кратким описанием, реализации получения данных с постраничной пагинацией. Покрытие кода тестами Junit5, Mockito;
4. Ветка [add-docker](https://github.com/Stepan4o/java_shareit/tree/add-docker) : Реализация микросервисной архитектуры. Приложение разбито на два модуля server и gateway. Настроен запуск приложения через Docker:
   - **shareit-gateway** http://localhost:8080 - перенесена оснавная логика обработки входных данных от пользователя для последующей передачи уже валидных данных в основной сервис 
   - **shareit-server** http://localhost:9090 - перенесена вся логика касающаяся обращений к базе данных.
   > Инструкция по развёртыванию приложения:
   >> mvn clean package / mvn install / docker-compouse build / docker-compouse up 

## Схема реляционной базы данных
  ![ShareIt_schema](https://github.com/Stepan4o/java_shareit/blob/main/ShareIt_schema.png)
