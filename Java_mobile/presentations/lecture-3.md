---
marp: true
theme: mobile-course
paginate: true
footer: «Лекция 3. Работа с локальной базой данных»
size: 16:9
---

<!-- _class: lead -->
<!-- _paginate: false -->

# Лекция 3. Работа с локальной базой данных

### Разработка мобильных приложений на Java

---

## О лекции

---

## Цель лекции

Научить студентов сохранять данные локально: простые настройки через SharedPreferences, структурированные записи через Room.

---

## План на 90 минут

| Время | Раздел |
|---:|---|
| 0-20 мин | SharedPreferences |
| 20-45 мин | Room и SQLite |
| 45-65 мин | Entity, DAO, Database |
| 65-85 мин | Практика: заметки |
| 85-90 мин | Итоги и домашнее задание |

---

## 1. Зачем нужно локальное хранение

Мобильное приложение не должно терять данные при закрытии экрана или приложения. Локальное хранение нужно для:

- настроек пользователя;
- кэша данных с сервера;
- заметок и черновиков;
- истории действий;
- работы без интернета.

В Android есть разные способы хранения:

---

## 1. Зачем нужно локальное хранение · продолжение

- `SharedPreferences` - небольшие пары ключ-значение;
- файлы - произвольные данные;
- SQLite - реляционная база данных;
- Room - удобная абстракция поверх SQLite.

---

## 2. SharedPreferences

`SharedPreferences` подходят для простых настроек:

- выбранная тема;
- имя пользователя;
- флаг первого запуска;
- последняя выбранная вкладка.

Актуальное уточнение: для новых Kotlin-проектов Android Developers рекомендует Jetpack DataStore как более современное решение для небольших наборов данных. В этом Java-курсе `SharedPreferences` изучаются потому, что они просты, часто встречаются в существующих Java-проектах и помогают понять идею key-value хранения. В конце темы важно сравнить их с DataStore и объяснить ограничения: возможные блокировки UI-потока, слабая обработка ошибок и отсутствие транзакционности.

---

## 2. SharedPreferences · продолжение

Сохранение:

```java
SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
preferences.edit()
        .putString("username", "Анна")
        .putBoolean("dark_theme", true)
        .apply();
```

Чтение:

---

## 2. SharedPreferences · продолжение

```java
SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
String username = preferences.getString("username", "Гость");
boolean darkTheme = preferences.getBoolean("dark_theme", false);
```

`apply()` сохраняет асинхронно. `commit()` сохраняет синхронно и возвращает результат, но для интерфейса обычно используют `apply()`.

Практический пример: сохранить имя пользователя.

---

## 2. SharedPreferences · продолжение

```java
EditText nameEditText = findViewById(R.id.nameEditText);
Button saveButton = findViewById(R.id.saveButton);

SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
nameEditText.setText(preferences.getString("username", ""));

saveButton.setOnClickListener(view -> {
    String username = nameEditText.getText().toString();
    preferences.edit().putString("username", username).apply();
});
```

---

## 3. Room

SQLite - встроенная реляционная база данных. С ней можно работать напрямую через SQL, но в Android-коде это быстро становится громоздким.

Room - библиотека Jetpack, которая дает удобный слой поверх SQLite:

- таблицы описываются Java-классами;
- запросы описываются в DAO-интерфейсах;
- Room проверяет SQL-запросы на этапе компиляции;
- данные можно получать как Java-объекты.

Компоненты Room:

---

## 3. Room · продолжение

- `Entity` - таблица базы данных;
- `DAO` - объект доступа к данным;
- `Database` - главный класс базы.

---

## 4. Подключение Room

В `build.gradle` модуля приложения для Java-проекта используются `room-runtime` и annotation processor. Номер версии нужно сверять с актуальной стабильной версией AndroidX Room перед проведением курса.

```gradle
def room_version = "2.6.1"

dependencies {
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
}
```

---

## 4. Подключение Room · продолжение

Если проект переводится на Kotlin, в современных материалах Google могут использоваться KSP, coroutines, Flow и `room-ktx`. Для данной лекции это не основная линия, но студентам полезно знать, что официальный Kotlin/Compose-путь выглядит иначе.

Если используется AndroidX, также нужны соответствующие зависимости AppCompat и RecyclerView.

---

## 5. Entity

Entity описывает таблицу.

---

## 5. Entity · продолжение

```java
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String text;
    public long createdAt;

    public Note(String title, String text, long createdAt) {
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
    }
}
```

---

## 5. Entity · продолжение

Здесь:

- `@Entity(tableName = "notes")` создает таблицу `notes`;
- `@PrimaryKey(autoGenerate = true)` задает автоинкрементный первичный ключ;
- поля класса становятся столбцами таблицы.

---

## 6. DAO

DAO описывает операции с таблицей.

---

## 6. DAO · продолжение

```java
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    List<Note> getAll();

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getById(int id);

    @Insert
    void insert(Note note);

```

---

## 6. DAO · продолжение

```java
    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
```

Room умеет генерировать реализацию этого интерфейса.

---

## 7. Database

```java
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
```

Создание базы:

---

## 7. Database · продолжение

```java
AppDatabase db = Room.databaseBuilder(
        getApplicationContext(),
        AppDatabase.class,
        "notes.db"
).build();
```

Важное правило: операции с базой нельзя выполнять в главном UI-потоке. Для учебного примера можно использовать `ExecutorService`.

---

## 7. Database · продолжение

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

executor.execute(() -> {
    List<Note> notes = db.noteDao().getAll();
    runOnUiThread(() -> {
        // обновить RecyclerView
    });
});
```

---

## 8. Практика: приложение «Заметки»

Минимальный сценарий:

1. Пользователь вводит заголовок и текст заметки.
2. Нажимает «Сохранить».
3. Заметка сохраняется в Room.
4. Список заметок обновляется.
5. При перезапуске приложения заметки остаются.

`activity_main.xml`:

---

## 8. Практика: приложение «Заметки» · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/titleEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Заголовок" />

    <EditText
        android:id="@+id/textEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
```

---

## 8. Практика: приложение «Заметки» · продолжение

```xml
        android:hint="Текст заметки" />

    <Button
        android:id="@+id/saveButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Сохранить" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/notesRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginTop="12dp"
        android:layout_weight="1" />

</LinearLayout>
```

---

## 8. Практика: приложение «Заметки» · продолжение

Фрагмент `MainActivity.java`:

---

## 8. Практика: приложение «Заметки» · продолжение

```java
public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private ExecutorService executor;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "notes.db").build();
        executor = Executors.newSingleThreadExecutor();

        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText textEditText = findViewById(R.id.textEditText);
        Button saveButton = findViewById(R.id.saveButton);
        RecyclerView recyclerView = findViewById(R.id.notesRecyclerView);

```

---

## 8. Практика: приложение «Заметки» · продолжение

```java
        adapter = new NoteAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString();
            String text = textEditText.getText().toString();

            if (title.trim().isEmpty()) {
                titleEditText.setError("Введите заголовок");
                return;
            }

            Note note = new Note(title, text, System.currentTimeMillis());
            executor.execute(() -> {
                db.noteDao().insert(note);
                List<Note> notes = db.noteDao().getAll();
                runOnUiThread(() -> {
```

---

## 8. Практика: приложение «Заметки» · продолжение

```java
                    adapter.setNotes(notes);
                    titleEditText.setText("");
                    textEditText.setText("");
                });
            });
        });

        loadNotes();
    }

    private void loadNotes() {
        executor.execute(() -> {
            List<Note> notes = db.noteDao().getAll();
            runOnUiThread(() -> adapter.setNotes(notes));
        });
    }
}
```

---

## 8. Практика: приложение «Заметки» · продолжение

Минимальная структура NoteAdapter:

---

## 8. Практика: приложение «Заметки» · продолжение

```java
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> notes;

    public NoteAdapter(List<Note> notes) {
        this.notes = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
```

---

## 8. Практика: приложение «Заметки» · продолжение

```java
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.title);
        holder.textTextView.setText(note.text);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView textTextView;

```

---

## 8. Практика: приложение «Заметки» · продолжение

```java
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
        }
    }
}
```

Минимальная разметка строки `item_note.xml`:

---

## 8. Практика: приложение «Заметки» · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/titleTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/textTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
```

---

## 8. Практика: приложение «Заметки» · продолжение

```xml
        android:layout_marginTop="4dp"
        android:textSize="14sp" />

</LinearLayout>
```

Для редактирования и удаления заметок добавьте в `NoteAdapter` слушатель кликов по аналогии с `StudentAdapter.OnStudentClickListener` из лекции 2.

Метод `setNotes()` добавляется внутрь `NoteAdapter`. Его лучше реализовать через `DiffUtil`, чтобы RecyclerView обновлял только изменившиеся строки, а не перерисовывал весь список:

---

## 8. Практика: приложение «Заметки» · продолжение

```java
public void setNotes(List<Note> newNotes) {
    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
            new NoteDiffCallback(this.notes, newNotes)
    );
    this.notes.clear();
    this.notes.addAll(newNotes);
    diffResult.dispatchUpdatesTo(this);
}
```

Минимальный `DiffUtil.Callback`:

---

## 8. Практика: приложение «Заметки» · продолжение

```java
public class NoteDiffCallback extends DiffUtil.Callback {
    private final List<Note> oldList;
    private final List<Note> newList;

    public NoteDiffCallback(List<Note> oldList, List<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }
```

---

## 8. Практика: приложение «Заметки» · продолжение

```java

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return Objects.equals(oldNote.title, newNote.title)
                && Objects.equals(oldNote.text, newNote.text)
                && oldNote.createdAt == newNote.createdAt;
    }
}
```

---

## 9. SharedPreferences в приложении «Заметки»

Пример сохранения настройки темы:

```java
SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
boolean darkTheme = preferences.getBoolean("dark_theme", false);

preferences.edit()
        .putBoolean("dark_theme", true)
        .apply();
```

---

## 9. SharedPreferences в приложении «Заметки» · продолжение

На этом этапе курса достаточно сохранить настройку и вывести ее значение на экран. Полная смена темы может быть вынесена как дополнительное задание. После выполнения примера преподаватель показывает современную рекомендацию: для новых приложений предпочтительнее DataStore, а `SharedPreferences` стоит воспринимать как базовый и legacy-совместимый механизм.

---

## Типичные ошибки

- выполнение запросов Room в UI-потоке;
- забыта зависимость `room-compiler`;
- забыта аннотация `@PrimaryKey`;
- версия базы изменена без миграции;
- студент использует SharedPreferences для сложных списков объектов;
- список RecyclerView обновляется через `notifyDataSetChanged()` там, где лучше применить `DiffUtil`;
- тема DataStore упоминается как полная замена Room, хотя DataStore подходит только для небольших key-value или типизированных настроек.

---

## Контрольные вопросы

1. Для каких данных подходят SharedPreferences и почему для новых проектов часто рекомендуют DataStore?
2. Почему Room удобнее прямого SQLite?
3. Что такое Entity?
4. Что такое DAO?
5. Почему нельзя выполнять запросы к базе в главном потоке?

---

## Домашнее задание

Доработать приложение «Заметки»:

- добавить удаление заметки;
- добавить редактирование заметки;
- добавить отображение даты создания;
- сохранить настройку «показывать сначала новые / сначала старые» в SharedPreferences.
