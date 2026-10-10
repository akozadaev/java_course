---
marp: true
theme: mobile-course
paginate: true
footer: «Итоговый контроль по курсу»
size: 16:9
---

<!-- _class: lead -->
<!-- _paginate: false -->

# Итоговый контроль по курсу

### Разработка мобильных приложений на Java

---

## О лекции

---

## Мини-проект

Разработать Android-приложение на Java «Личная библиотека».

Минимальные требования:

- экран списка книг через RecyclerView;
- экран детальной информации о книге;
- локальное сохранение избранных книг в Room;
- настройка имени пользователя или темы через SharedPreferences;
- загрузка списка книг из REST API через Retrofit;
- кэширование последнего успешного ответа REST API в Room;
- обработка загрузки, пустого списка и ошибки;
- сохранение состояния экрана при повороте.

---

## Критерии оценивания

| Критерий | Баллы |
|---|---:|
| Проект запускается без ошибок | 10 |
| Корректная структура Activity и layout | 15 |
| RecyclerView и Adapter работают корректно | 15 |
| Используется Room для локальных данных | 20 |
| Используется Retrofit для REST-запросов | 15 |
| Реализовано кэширование REST-данных в Room | 10 |
| Обработка ошибок и состояний загрузки | 10 |
| Читаемость кода и ресурсов | 5 |

Максимум: 100 баллов.

---

## Рекомендуемые ресурсы

### Современные материалы Google Learning и Android Developers

- [Android Developers Training: курсы и pathways](https://developer.android.com/courses).
- [Android Developers Training: Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course).
- [Android Developers codelab: Create your first Android app](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app).
- [Android Developers: The activity lifecycle (Views)](https://developer.android.com/topic/libraries/architecture/views/activity-lifecycle-views).
- [Android Developers codelab: Stages of the Activity lifecycle](https://developer.android.com/codelabs/basic-android-kotlin-compose-activity-lifecycle).

---

## Рекомендуемые ресурсы · продолжение

- [Android Developers: Save simple data with SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences).
- [Android Developers: DataStore guide](https://developer.android.com/topic/libraries/architecture/datastore).
- [Android Developers: Save data in a local database using Room](https://developer.android.com/training/data-storage/room).
- [Android Developers codelab: Persist data with Room](https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room).
- [Android Developers codelab: Get data from the internet](https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet).

### Материалы для Java/XML-линии курса

---

## Рекомендуемые ресурсы · продолжение

- Android Developers Java codelabs из серии Android Developer Fundamentals, если они доступны в текущем архиве документации.
- [Android Developers Material Components codelabs for Java, например MDC-102 Android: Material Structure and Layout (Java)](https://developer.android.com/codelabs/mdc-102-java).
- Android Developers: RecyclerView documentation.
- Android Developers: App architecture guide.

### Дополнительные библиотеки и примеры

---

## Рекомендуемые ресурсы · продолжение

- Square Retrofit documentation and GitHub repository.
- OkHttp documentation.
- Gson documentation.
- Пример `akozadaev/librarySpringMobileClient`.
