# Тема: REST-клиент

**Цель темы.** Retrofit + модель + состояния UI; кэш при ошибке сети опционально/желательно.

**Общая постановка.** BASE_URL со `/`. HTTPS предпочтителен; cleartext только с объяснением. Обработать loading/empty/error. getActiveNetworkInfo — не использовать как основной путь. Book/DTO с конструктором для POST при необходимости. Потокобезопасный RetrofitClient.

## Варианты

### Вариант 1. Клиент библиотеки

**Задание.** Сценарий «Клиент библиотеки». Список, поиск, деталь по id; кэш последнего списка в Room при ошибке сети. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Пустой поиск — сообщение; offline показывает кэш.

### Вариант 2. Публичный JSON API (типичный)

**Задание.** Сценарий «Публичный JSON API (типичный)». Например jsonplaceholder posts: list+detail (зафиксировать URL в README). Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Таймаут/ошибка DNS обработаны; нет краша на null body.

### Вариант 3. Погода (мок-сервер или публичный)

**Задание.** Сценарий «Погода (мок-сервер или публичный)». Экран города → температура; состояния UI. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** API key не коммитить; если нужен — local.properties.

### Вариант 4. Новости RSS→упрощённо JSON

**Задание.** Сценарий «Новости RSS→упрощённо JSON». Лента заголовков; pull-to-refresh SwipeRefreshLayout. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Повторный refresh не плодит адаптеры.

### Вариант 5. GitHub users search (публичный API)

**Задание.** Сценарий «GitHub users search (публичный API)». Поиск login; деталь; rate limit → понятная ошибка. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** User-Agent/заголовки по требованиям API; README.

### Вариант 6. CRUD учебный mockapi/interceptor

**Задание.** Сценарий «CRUD учебный mockapi/interceptor». GET список + POST создание с телом; Optimistic UI optional. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Конструктор DTO для @Body; 4xx отдельно от network fail.

### Вариант 7. Пагинация page/limit

**Задание.** Сценарий «Пагинация page/limit». Подгрузка следующей страницы (кнопка или scroll listener упрощённо). Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Дубликаты не появляются; loading footer/флаг.

### Вариант 8. Офлайн-first очередь

**Задание.** Сценарий «Офлайн-first очередь». Локально создать сущность, пометить dirty; при сети POST. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** После успеха dirty сбрасывается; политика конфликтов в README.

### Вариант 9. Картинка Glide/Coil-Java

**Задание.** Сценарий «Картинка Glide/Coil-Java». Список с url картинки; placeholder/error drawable. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Нет OOM на больших списках (size override); HTTPS.

### Вариант 10. Единый error mapper

**Задание.** Сценарий «Единый error mapper». Преобразование Throwable/Response → UiError sealed/enum. Создайте Android-проект (Empty Views Activity, Java + XML) по общим правилам `assignments/README.md`; в README варианта укажите minSdk/compileSdk, как запускать и что проверено на эмуляторе или устройстве.

**Критерии приёмки.** Одинаковые сообщения на list и detail; тестов достаточно unit на mapper.

