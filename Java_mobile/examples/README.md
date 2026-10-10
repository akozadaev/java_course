# Демонстрационные Android-приложения

| Лекция | Проект | Что демонстрирует |
|---:|---|---|
| 0 | `student-business-card` | Java/XML, ресурсы и первый экран |
| 1 | `counter-app` | обработчики, состояние и жизненный цикл Activity |
| 2 | `students-app` | RecyclerView, Adapter, Intent и Activity Result API |
| 3 | `notes-app` | Room, DAO, фоновые операции и SharedPreferences |
| 4 | `library-client` | Retrofit, REST, поиск и состояния интерфейса |

Каждый каталог является отдельным проектом Android Studio. Откройте нужный каталог через **File → Open**, дождитесь Gradle Sync и запустите модуль `app`.

Проекты используют Android SDK 35 и JDK 17.

## Android Studio в Windows, проекты в WSL

Сначала скопируйте каталог `examples` на диск Windows. После обновления примеров
старую копию нужно заменить, потому что в проекты добавлен Gradle Wrapper:

```bash
cp -r examples /mnt/c/Users/<имя>/AndroidStudioProjects/course-examples
```

Открывайте конкретный каталог, например `course-examples/student-business-card`,
а не общий `course-examples`. При первом открытии выберите **Trust Project** и
дождитесь окончания Gradle Sync. После успешной синхронизации Android Studio
автоматически обнаружит модуль `app` и создаст Android-конфигурацию запуска.
