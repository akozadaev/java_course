# Лекция 4 — REST-клиент библиотеки

Retrofit загружает книги из Gutenberg API через RapidAPI. Доступны стартовый каталог и поиск. Интерфейс явно показывает загрузку, пустой ответ, ошибку сети и ошибку HTTP.

## Пример работы

После успешной загрузки экран «Каталог книг» показывает список названий и авторов.
На предоставленном скриншоте видны, например, «Pride and Prejudice» — Austen, Jane,
«Moby Dick; Or, The Whale» — Melville, Herman и «Frankenstein; or, the modern Prometheus» — Shelley, Mary Wollstonecraft.
Над списком расположены поле «Название или автор» и кнопка «НАЙТИ».

## Настройка API

Получите ключ и подключите доступ к сервису через https://gutenbergapi.com/.
Добавьте в корневой `local.properties` строку `rapidapi.key=ВАШ_КЛЮЧ`,
сохранив существующую настройку `sdk.dir`, и пересоберите приложение.
Без ключа приложение покажет сообщение о необходимости настройки.

Базовый адрес: `https://project-gutenberg-free-books-api1.p.rapidapi.com/`.
Запросы используют `/books`, параметр поиска `q` и `include_removed=true`
согласно предоставленному примеру из RapidAPI.
Заголовок `X-RapidAPI-Host`: `project-gutenberg-free-books-api1.p.rapidapi.com`.
Ключ исключён из Git вместе с `local.properties`, но попадает в APK.
Для публичного приложения храните ключ на своём сервере.

## Права доступа в Linux / WSL

Для текущего проекта настроены следующие права:

- Каталоги: `755`.
- Обычные файлы: `644`.
- Скрипт `gradlew`: `755`, чтобы его можно было запускать.
- Файл `local.properties`: `600`, чтобы настройки и ключ API были доступны только владельцу.

При необходимости восстановить эти права выполните из корня проекта:

```bash
find . -type d -exec chmod 755 {} +
find . -type f -exec chmod 644 {} +
chmod 755 gradlew
chmod 600 local.properties
```