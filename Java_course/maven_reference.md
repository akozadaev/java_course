# 📘 Справочник команд и ключей Maven

## 📌 Синтаксис запуска

```
mvn [options] [<goal(s)>] [<phase(s)>]
```

Полный список опций: `mvn -h`.

---

## 🔄 Жизненные циклы и фазы

### 1. `clean`

| Фаза | Описание |
|------|----------|
| `pre-clean` | Действия перед очисткой |
| `clean` | Удаление артефактов предыдущей сборки (`target/`) |
| `post-clean` | Действия после очистки |

### 2. `default`

| № | Фаза | Что делает | Привязанный плагин:цель |
|---|------|------------|--------------------------|
| 1 | `validate` | Проверка корректности `pom.xml` | — |
| 2 | `initialize` | Инициализация параметров сборки | — |
| 3 | `generate-sources` | Генерация исходного кода | — |
| 4 | `process-sources` | Обработка исходного кода | — |
| 5 | `generate-resources` | Генерация ресурсов | — |
| 6 | `process-resources` | Копирование ресурсов в `target/classes` | `resources:resources` |
| 7 | `compile` | Компиляция `src/main/java` | `compiler:compile` |
| 8 | `process-classes` | Постобработка скомпилированных классов | — |
| 9 | `generate-test-sources` | Генерация тестового кода | — |
| 10 | `process-test-sources` | Обработка тестового кода | — |
| 11 | `generate-test-resources` | Генерация тестовых ресурсов | — |
| 12 | `process-test-resources` | Копирование тестовых ресурсов | `resources:testResources` |
| 13 | `test-compile` | Компиляция `src/test/java` | `compiler:testCompile` |
| 14 | `process-test-classes` | Постобработка тестовых классов | — |
| 15 | `test` | Запуск **юнит-тестов** | `surefire:test` |
| 16 | `prepare-package` | Подготовка к упаковке | — |
| 17 | `package` | Упаковка в JAR / WAR / EAR | `jar:jar`, `war:war` и др. |
| 18 | `pre-integration-test` | Подготовка к интеграционным тестам | — |
| 19 | `integration-test` | Запуск интеграционных тестов | `failsafe:integration-test` * |
| 20 | `post-integration-test` | Завершение интеграционных тестов | `failsafe:verify` * |
| 21 | `verify` | Проверка качества сборки | — |
| 22 | `install` | Установка артефакта в локальный репозиторий `~/.m2/repository` | `install:install` |
| 23 | `deploy` | Публикация артефакта в удалённый репозиторий | `deploy:deploy` |

> **\*** Важное уточнение: `failsafe` **не привязывается автоматически** (в отличие от `surefire` в фазе `test`). Чтобы интеграционные тесты (`*IT.java`) запускались, плагин `maven-failsafe-plugin` нужно явно объявить в `<build><plugins>` с целями `integration-test` и `verify`. Иначе фаза `integration-test` просто ничего не сделает.

Привязки остальных плагинов зависят от типа упаковки (`jar`, `war`, `pom` и т.д.).

### 3. `site`

| Фаза | Описание |
|------|----------|
| `pre-site` | Действия перед генерацией сайта |
| `site` | Генерация сайта проекта |
| `post-site` | Действия после генерации сайта |
| `site-deploy` | Публикация сайта на сервере |

---

## 🛠️ Опции командной строки

### Основные

| Короткая | Длинная | Описание |
|----------|---------|----------|
| `-h` | `--help` | Справка |
| `-v` | `--version` | Версия Maven и Java |
| `-V` | `--show-version` | Версия + продолжить сборку |
| `-q` | `--quiet` | Только ошибки |
| `-X` | `--debug` | Подробный отладочный вывод |
| `-e` | `--errors` | Расширенные сообщения об ошибках |
| `-B` | `--batch-mode` | Неинтерактивный режим (CI/CD) |
| `-o` | `--offline` | Без обращения к удалённым репозиториям |
| `-N` | `--non-recursive` | Не рекурсировать в подпроекты |

### Управление сборкой

| Короткая | Длинная | Описание |
|----------|---------|----------|
| `-f` | `--file <arg>` | Альтернативный `pom.xml` или директория |
| `-pl` | `--projects <arg>` | Список модулей реактора: `groupId:artifactId` или относительный путь |
| `-am` | `--also-make` | Собрать также модули, от которых зависят выбранные |
| `-amd` | `--also-make-dependents` | Собрать также модули, которые зависят от выбранных |
| `-rf` | `--resume-from <arg>` | Возобновить сборку с указанного модуля |
| `-T` | `--threads <arg>` | Потоки: `2`, `2.0C` (C = число ядер) |

> ⚠️ Флаги `-pl`, `-am`, `-amd`, `-rf`, `-T` работают **только внутри реактора** — когда есть агрегирующий parent-pom с `<modules>`. В независимом одиночном проекте `-pl` вызовет ошибку `Could not find the selected project in the reactor`.

### Управление отказами

| Короткая | Длинная | Описание |
|----------|---------|----------|
| `-ff` | `--fail-fast` | Остановиться при первой ошибке |
| `-fae` | `--fail-at-end` | Продолжить, упасть в конце |
| `-fn` | `--fail-never` | Никогда не завершать с ошибкой |

### Профили и настройки

| Короткая | Длинная | Описание |
|----------|---------|----------|
| `-P` | `--activate-profiles <arg>` | Активировать профили (через запятую) |
| `-s` | `--settings <arg>` | Альтернативный файл пользовательских настроек |
| `-gs` | `--global-settings <arg>` | Альтернативный глобальный файл настроек |
| `-t` | `--toolchains <arg>` | Альтернативный файл toolchains |
| `-gt` | `--global-toolchains <arg>` | Альтернативный глобальный файл toolchains |

### Свойства, кэш, безопасность

| Короткая | Длинная | Описание |
|----------|---------|----------|
| `-D` | `--define <arg>` | Системное свойство: `-Dkey=value` |
| `-U` | `--update-snapshots` | Принудительно обновить SNAPSHOT-зависимости |
| `-nsu` | `--no-snapshot-updates` | Подавить обновление SNAPSHOT |
| `-C` | `--strict-checksums` | Ошибка при несовпадении контрольных сумм |
| `-c` | `--lax-checksums` | Предупреждение при несовпадении контрольных сумм |
| `-l` | `--log-file <arg>` | Записывать вывод в файл |
| `-emp` | `--encrypt-master-password <arg>` | Зашифровать мастер-пароль |
| `-ep` | `--encrypt-password <arg>` | Зашифровать пароль сервера |

### Устаревшие (no-op, оставлены для совместимости)

`-cpu` / `--check-plugin-updates`, `-npu` / `--no-plugin-updates`, `-npr` / `--no-plugin-registry`, `-up` / `--update-plugins` — не выполняют никаких действий.

---

## 🧩 Основные команды

| Команда | Описание |
|---------|----------|
| `mvn clean` | Очистить проект (`target/`) |
| `mvn compile` | Скомпилировать исходный код |
| `mvn test` | Запустить **юнит-тесты** |
| `mvn package` | Упаковать (JAR/WAR) |
| `mvn verify` | Выполнить проверки после упаковки |
| `mvn install` | Установить в локальный репозиторий |
| `mvn deploy` | Опубликовать в удалённый репозиторий |
| `mvn clean install` | Полный цикл: очистка + сборка + установка |
| `mvn clean install -U` | То же + принудительное обновление SNAPSHOT-зависимостей |
| `mvn clean deploy site-deploy` | Полная сборка, публикация и деплой сайта |

### Полезные цели плагинов

| Команда | Описание |
|---------|----------|
| `mvn archetype:generate` | Создать проект из шаблона |
| `mvn dependency:tree` | Дерево зависимостей |
| `mvn dependency:list` | Список зависимостей |
| `mvn dependency:analyze` | Анализ неиспользуемых/отсутствующих зависимостей |
| `mvn dependency:purge-local-repository` | Перекачать зависимости с нуля (лечит «битый» кэш) |
| `mvn versions:display-dependency-updates` | Проверить доступные обновления |
| `mvn checkstyle:check` | Проверка стиля кода |
| `mvn help:describe -Dplugin=<name>` | Описание плагина/цели |
| `mvn help:effective-pom` | Итоговый `pom.xml` с учётом наследования и профилей |
| `mvn help:effective-settings` | Итоговый `settings.xml` |

---

## 💡 Типовые сценарии

### Шаг 0. Определить, есть ли реактор

```bash
cd Java_course/examples
ls pom.xml 2>/dev/null && echo "Есть агрегирующий parent-pom → Вариант 2" \
                       || echo "Нет parent-pom → Вариант 1"
```

---

### Вариант 1. Проекты НЕ связаны (у каждого свой `pom.xml`)

```bash
# Сборка одного проекта — переходим в его директорию
cd Java_course/examples/lecture-02-maven-code-quality
mvn clean package

# Тесты другого проекта
cd ../lecture-15-junit-testing
mvn test

# Принудительно обновить SNAPSHOT-зависимости
mvn clean install -U
```

> `-pl`, `-am`, `-amd`, `-fae` здесь **не работают** — реактора нет. Обход по всем проектам — только внешним скриптом (`for d in */; do (cd "$d" && mvn -q clean package); done`).

---

### Вариант 2. Проекты СВЯЗАНЫ (parent-pom в `Java_course/examples`)

```bash
# Всегда запускаем из корня реактора
cd Java_course/examples

# Собрать один модуль (путь указывается относительно корня реактора)
mvn -pl lecture-02-maven-code-quality clean package

# Тесты одного модуля
mvn -pl lecture-15-junit-testing test

# Альтернативная форма через artifactId
mvn -pl :lecture-15-junit-testing test

# Модуль + все его зависимости внутри реактора
mvn -pl lecture-20-spring-data-jpa -am package

# Модуль + всё, что зависит от него
mvn -pl lecture-05-core -amd package

# Продолжить сборку, несмотря на упавшие модули (CI/CD)
mvn -fae clean install

# Возобновить с определённого модуля
mvn -rf lecture-15-junit-testing install

# Параллельная сборка
mvn -T 2C clean install

# Обновить SNAPSHOT-зависимости во всех модулях
mvn clean install -U
```

---

## 🩺 Частые проблемы и их решение

| Проблема | Решение |
|----------|---------|
| Странные ошибки `ClassNotFoundException` / `NoSuchMethodError` после обновления зависимостей | `mvn clean install -U` — очистка `target/` и обновление SNAPSHOT |
| Maven кэширует старую версию зависимости, изменения в `pom.xml` «не видны» | `mvn dependency:purge-local-repository` |
| Нужно узнать, какая версия плагина/зависимости реально используется | `mvn help:effective-pom` |
| Нужно понять, откуда пришла транзитивная зависимость | `mvn dependency:tree -Dincludes=groupId:artifactId` |
| `OutOfMemoryError` при компиляции большого проекта | `mvn compile -Dmaven.compiler.fork=true -Dmaven.compiler.meminitial=256m -Dmaven.compiler.maxmem=1024m` |
| `Could not find the selected project in the reactor` | Вы запускаете `-pl` без реактора → `cd` в директорию проекта, либо запускайте из корня с parent-pom |
| Сборка идёт, но интеграционные тесты не запускаются | `maven-failsafe-plugin` не объявлен — добавьте его в `<build><plugins>` |
| Нужно быстро увидеть версию Maven/Java на CI | `mvn -V -B` |
