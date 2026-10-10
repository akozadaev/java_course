---
marp: true
theme: mobile-course
paginate: true
footer: «Лекция 1. Разработка простого приложения на Java»
size: 16:9
---

<!-- _class: lead -->
<!-- _paginate: false -->

# Лекция 1. Разработка простого приложения на Java

### Разработка мобильных приложений на Java

---

## О лекции

---

## Цель лекции

Познакомить студентов с основами Java в контексте Android-разработки и закрепить понимание Activity через создание приложения-счетчика.

---

## План на 90 минут

| Время | Раздел |
|---:|---|
| 0-20 мин | Минимум Java для Android |
| 20-35 мин | Структура Android-проекта |
| 35-60 мин | Activity и жизненный цикл |
| 60-85 мин | Практика: приложение-счетчик |
| 85-90 мин | Итоги и домашнее задание |

---

## 1. Основы Java для Android

Java - объектно-ориентированный язык. В Android Java используется для описания логики приложения: обработчиков событий, работы с данными, сетевых запросов, базы данных и взаимодействия с системой.

### Переменные и типы

```java
int count = 0;
double price = 19.99;
boolean isEnabled = true;
String title = "Android";
```

---

## 1. Основы Java для Android · продолжение

`int` хранит целые числа, `double` - дробные, `boolean` - истину или ложь, `String` - текст.

### Условия

```java
if (count == 0) {
    message = "Счетчик пуст";
} else {
    message = "Значение: " + count;
}
```

---

## 1. Основы Java для Android · продолжение

### Методы

Метод - именованный блок кода.

```java
private String formatCount(int value) {
    return "Значение счетчика: " + value;
}
```

### Классы и объекты

Класс описывает тип объекта. Объект - конкретный экземпляр класса.

---

## 1. Основы Java для Android · продолжение

```java
public class Student {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

---

## 1. Основы Java для Android · продолжение

Использование:

```java
Student student = new Student("Анна", 22);
String name = student.getName();
```

### Наследование

`MainActivity` наследуется от `AppCompatActivity`, поэтому получает поведение Android-экрана.

---

## 1. Основы Java для Android · продолжение

```java
public class MainActivity extends AppCompatActivity {
}
```

### Интерфейсы

Интерфейс задает набор методов, которые должен реализовать класс. В Android часто встречаются слушатели событий.

---

## 1. Основы Java для Android · продолжение

```java
button.setOnClickListener(view -> {
    count++;
    countTextView.setText(String.valueOf(count));
});
```

В старых проектах, где Java 8 desugaring не настроен, может встретиться классический синтаксис без lambda-выражений:

---

## 1. Основы Java для Android · продолжение

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        count++;
        countTextView.setText(String.valueOf(count));
    }
});
```

---

## 2. Структура Android-проекта

`MainActivity.java` содержит код экрана. Обычно в нем:

- вызывается `setContentView()`;
- находятся элементы интерфейса через `findViewById()`;
- назначаются обработчики событий;
- запускаются другие экраны;
- вызываются методы работы с данными.

Пример:

---

## 2. Структура Android-проекта · продолжение

```java
TextView titleTextView = findViewById(R.id.titleTextView);
Button saveButton = findViewById(R.id.saveButton);
```

`AndroidManifest.xml` описывает приложение:

---

## 2. Структура Android-проекта · продолжение

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:label="@string/app_name"
        android:theme="@style/Theme.CounterApp">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>
</manifest>
```

---

## 2. Структура Android-проекта · продолжение

Смысл `MAIN` и `LAUNCHER`: эта Activity запускается при нажатии на иконку приложения.

Ресурсы позволяют не хранить строки, цвета и размеры прямо в коде.

`strings.xml`:

---

## 2. Структура Android-проекта · продолжение

```xml
<resources>
    <string name="app_name">Counter App</string>
    <string name="increment">Увеличить</string>
    <string name="reset">Сбросить</string>
</resources>
```

Использование в XML:

```xml
android:text="@string/increment"
```

---

## 3. Activity и жизненный цикл

Activity - это один экран приложения. Пользователь видит Activity, взаимодействует с ней, затем может перейти на другой экран, свернуть приложение или закрыть его.

Основные методы жизненного цикла:

---

## 3. Activity и жизненный цикл · продолжение

- `onCreate()` - Activity создается, здесь инициализируют интерфейс;
- `onStart()` - Activity становится видимой;
- `onResume()` - Activity готова к взаимодействию;
- `onPause()` - Activity частично теряет фокус;
- `onStop()` - Activity больше не видна;
- `onRestart()` - вызывается перед повторным `onStart()`, когда Activity возвращается из состояния Stopped;
- `onDestroy()` - Activity уничтожается.

Пример логирования. При обычном первом запуске последовательность будет `onCreate()` -> `onStart()` -> `onResume()`. При возврате из остановленного состояния будет ветка `onStop()` -> `onRestart()` -> `onStart()` -> `onResume()`:

---

## 3. Activity и жизненный цикл · продолжение

```java
private static final String TAG = "MainActivity";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    setContentView(R.layout.activity_main);
}

@Override
protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart");
}

@Override
protected void onStart() {
    super.onStart();
```

---

## 3. Activity и жизненный цикл · продолжение

```java
    Log.d(TAG, "onStart");
}

@Override
protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
}

@Override
protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause");
}

@Override
protected void onStop() {
    super.onStop();
```

---

## 3. Activity и жизненный цикл · продолжение

```java
    Log.d(TAG, "onStop");
}

@Override
protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
}
```

На занятии нужно показать Logcat и выполнить действия:

---

## 3. Activity и жизненный цикл · продолжение

- запуск приложения;
- сворачивание приложения;
- возврат в приложение;
- поворот экрана;
- закрытие приложения.

При повороте экрана Activity часто пересоздается. Поэтому значение обычной переменной может потеряться.

---

## 3. Activity и жизненный цикл · продолжение

В манифесте можно изменить обработку отдельных configuration changes через `android:configChanges`, например для ориентации или размера экрана. Но для начального курса это не основной способ решения: чаще правильнее научиться сохранять состояние и корректно переживать пересоздание Activity, потому что изменение конфигурации может быть вызвано не только поворотом, но и сменой языка, темы, размера окна и другими системными событиями.

Пример, который полезно показать как справочный, но не использовать как главное решение:

---

## 3. Activity и жизненный цикл · продолжение

```xml
<activity
    android:name=".MainActivity"
    android:configChanges="orientation|screenSize" />
```

---

## 4. Сохранение состояния

Для временного сохранения состояния при пересоздании Activity используется `onSaveInstanceState()`.

```java
private static final String KEY_COUNT = "count";
private int count = 0;

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_COUNT, count);
}
```

---

## 4. Сохранение состояния · продолжение

Восстановление:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState != null) {
        count = savedInstanceState.getInt(KEY_COUNT, 0);
    }
}
```

---

## 4. Сохранение состояния · продолжение

Важно различать:

- временное состояние экрана - `onSaveInstanceState()`;
- пользовательские настройки - `SharedPreferences`;
- структурированные данные - база данных Room.

---

## 5. Практика: приложение-счетчик

`activity_main.xml`:

---

## 5. Практика: приложение-счетчик · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/countTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="0"
        android:textSize="48sp" />

    <Button
        android:id="@+id/incrementButton"
        android:layout_width="match_parent"
```

---

## 5. Практика: приложение-счетчик · продолжение

```xml
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="@string/increment" />

    <Button
        android:id="@+id/resetButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="@string/reset" />

</LinearLayout>
```

---

## 5. Практика: приложение-счетчик · продолжение

`MainActivity.java`:

---

## 5. Практика: приложение-счетчик · продолжение

```java
package com.example.counterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_COUNT = "count";

    private TextView countTextView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

```

---

## 5. Практика: приложение-счетчик · продолжение

```java
        countTextView = findViewById(R.id.countTextView);
        Button incrementButton = findViewById(R.id.incrementButton);
        Button resetButton = findViewById(R.id.resetButton);

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT, 0);
        }

        updateCountText();

        incrementButton.setOnClickListener(view -> {
            count++;
            updateCountText();
        });

        resetButton.setOnClickListener(view -> {
            count = 0;
            updateCountText();
```

---

## 5. Практика: приложение-счетчик · продолжение

```java
        });
    }

    private void updateCountText() {
        countTextView.setText(String.valueOf(count));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COUNT, count);
    }
}
```

---

## Типичные ошибки

- студент объявил локальную переменную `int count` внутри `onCreate()`, и обработчики не могут с ней работать;
- забыто `findViewById()`;
- в XML указан один `id`, а в Java используется другой;
- при повороте экрана значение не восстанавливается;
- строка задана напрямую в XML вместо ресурса.

---

## Контрольные вопросы

1. Что такое Activity?
2. Почему `onCreate()` не должен выполнять долгие операции?
3. Что произойдет с Activity при повороте экрана и почему `configChanges` не стоит считать универсальным решением?
4. Чем поле класса отличается от локальной переменной?
5. Для чего нужен `Bundle` в `onSaveInstanceState()`?

---

## Домашнее задание

Расширить приложение-счетчик:

- добавить кнопку уменьшения значения;
- запретить уход значения ниже нуля;
- добавить `TextView` со статусом: «ноль», «малое значение», «большое значение»;
- сохранить состояние при повороте экрана.
