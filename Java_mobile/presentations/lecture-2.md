---
marp: true
theme: mobile-course
paginate: true
footer: «Лекция 2. Экраны: UI и навигация»
size: 16:9
---

<!-- _class: lead -->
<!-- _paginate: false -->

# Лекция 2. Экраны: UI и навигация

### Разработка мобильных приложений на Java

---

## О лекции

---

## Цель лекции

Научить студентов создавать пользовательские интерфейсы на XML, отображать списки через RecyclerView и организовывать переходы между экранами.

---

## План на 90 минут

| Время | Раздел |
|---:|---|
| 0-20 мин | Layout-файлы и View |
| 20-45 мин | RecyclerView |
| 45-65 мин | Activity, Fragment и навигация |
| 65-85 мин | Практика: список студентов |
| 85-90 мин | Итоги и домашнее задание |

---

## 1. Layout-файлы и View

Интерфейс Android-приложения обычно состоит из View и ViewGroup.

`View` - базовый элемент интерфейса: текст, кнопка, изображение, поле ввода.

`ViewGroup` - контейнер, который размещает внутри себя другие элементы.

Основные элементы:

---

## 1. Layout-файлы и View · продолжение

- `TextView` - текст;
- `EditText` - ввод текста;
- `Button` - кнопка;
- `ImageView` - изображение;
- `RecyclerView` - список;
- `LinearLayout` - размещение по вертикали или горизонтали;
- `ConstraintLayout` - гибкое размещение через ограничения.

Пример формы:

---

## 1. Layout-файлы и View · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <EditText
        android:id="@+id/nameEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Имя студента" />

    <Button
        android:id="@+id/saveButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="12dp"
```

---

## 1. Layout-файлы и View · продолжение

```xml
        android:text="Сохранить" />

</LinearLayout>
```

Главные атрибуты:

---

## 1. Layout-файлы и View · продолжение

- `android:id` - идентификатор элемента;
- `android:layout_width` - ширина;
- `android:layout_height` - высота;
- `android:text` - текст;
- `android:hint` - подсказка для поля ввода;
- `android:padding` - внутренний отступ;
- `android:layout_marginTop` - внешний отступ сверху.

---

## 2. RecyclerView

Список - одна из самых частых задач в мобильных приложениях: список сообщений, книг, товаров, студентов, заметок.

`RecyclerView` эффективнее простого набора `TextView`, потому что переиспользует элементы списка. На экране одновременно видны, например, 10 элементов, и RecyclerView не создает 1000 View для 1000 записей сразу.

Компоненты RecyclerView:

- `RecyclerView` - сам список;
- `Adapter` - связывает данные с View;
- `ViewHolder` - хранит ссылки на элементы одной строки;
- `LayoutManager` - определяет способ расположения элементов.

---

## 2. RecyclerView · продолжение

### Модель данных

---

## 2. RecyclerView · продолжение

```java
public class Student {
    private final String name;
    private final String group;

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
}
```

---

## 2. RecyclerView · продолжение

### Layout элемента списка

`item_student.xml`:

---

## 2. RecyclerView · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/groupTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
```

---

## 2. RecyclerView · продолжение

```xml
        android:layout_marginTop="4dp"
        android:textSize="14sp" />

</LinearLayout>
```

### Adapter

Для примеров с @NonNull и @Nullable нужны импорты:

---

## 2. RecyclerView · продолжение

```java
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
```

---

## 2. RecyclerView · продолжение

```java
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    private final List<Student> students;
    private final OnStudentClickListener listener;

    public StudentAdapter(List<Student> students, OnStudentClickListener listener) {
        this.students = students;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
```

---

## 2. RecyclerView · продолжение

```java
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.nameTextView.setText(student.getName());
        holder.groupTextView.setText(student.getGroup());
        holder.itemView.setOnClickListener(view -> listener.onStudentClick(student));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
```

---

## 2. RecyclerView · продолжение

```java
        TextView nameTextView;
        TextView groupTextView;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            groupTextView = itemView.findViewById(R.id.groupTextView);
        }
    }
}
```

---

## 3. Навигация между экранами

В Android переход между Activity выполняется через `Intent`.

Пример запуска второго экрана:

```java
Intent intent = new Intent(MainActivity.this, DetailActivity.class);
intent.putExtra("name", student.getName());
intent.putExtra("group", student.getGroup());
startActivity(intent);
```

Получение данных во втором экране:

---

## 3. Навигация между экранами · продолжение

```java
String name = getIntent().getStringExtra("name");
String group = getIntent().getStringExtra("group");
```

В манифесте нужно зарегистрировать вторую Activity:

```xml
<activity android:name=".DetailActivity" />
```

### Возврат результата с экрана

---

## 3. Навигация между экранами · продолжение

В старых материалах Android часто встречается `startActivityForResult()`. Сейчас для новых проектов предпочтительнее Activity Result API. В Java это выглядит так:

---

## 3. Навигация между экранами · продолжение

```java
private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String selectedName = result.getData().getStringExtra("selected_name");
                Toast.makeText(this, "Выбран: " + selectedName, Toast.LENGTH_SHORT).show();
            }
        }
);
```

Запуск экрана деталей:

---

## 3. Навигация между экранами · продолжение

```java
Intent intent = new Intent(MainActivity.this, DetailActivity.class);
intent.putExtra("name", student.getName());
detailLauncher.launch(intent);
```

Возврат результата из `DetailActivity`:

---

## 3. Навигация между экранами · продолжение

```java
Intent resultIntent = new Intent();
resultIntent.putExtra("selected_name", name);
setResult(RESULT_OK, resultIntent);
finish();
```

`startActivityForResult()` можно упомянуть при чтении старого кода, но в практической работе лучше использовать Activity Result API.

---

## 4. Fragment

Fragment - это часть интерфейса и логики, которую можно поместить внутрь Activity. Фрагменты удобны, когда экран нужно разбить на независимые части или переиспользовать блок интерфейса.

Типичные случаи:

- на телефоне список и детали открываются на разных экранах;
- на планшете список и детали показываются рядом;
- нижняя навигация переключает несколько фрагментов внутри одной Activity.

Упрощенный жизненный цикл Fragment:

---

## 4. Fragment · продолжение

- `onAttach()` - Fragment связан с Activity;
- `onCreate()` - создана логика Fragment;
- `onCreateView()` - создана разметка;
- `onViewCreated()` - можно работать с элементами интерфейса;
- `onDestroyView()` - View уничтожена.

Пример Fragment:

---

## 4. Fragment · продолжение

```java
public class StudentsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_students, container, false);
    }
}
```

---

## 4. Fragment · продолжение

Минимальная разметка `fragment_students.xml` может быть любой. Для первого запуска достаточно одного `TextView`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:text="Список студентов" />
```

Чтобы поместить Fragment в Activity, в layout добавляют контейнер:

---

## 4. Fragment · продолжение

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/fragmentContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Затем Activity выполняет транзакцию через `FragmentManager`:

---

## 4. Fragment · продолжение

```java
if (savedInstanceState == null) {
    getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, new StudentsFragment())
            .commit();
}
```

Такой пример полезен перед переходом к Navigation Component: студенты видят базовый механизм добавления и замены фрагментов.

---

## 4. Fragment · продолжение

Для начального курса достаточно понимать, что Activity - экран верхнего уровня, а Fragment - переиспользуемая часть экрана.

---

## 5. Практика: список студентов

`activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/studentsRecyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

`MainActivity.java`:

---

## 5. Практика: список студентов · продолжение

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.studentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Student> students = new ArrayList<>();
        students.add(new Student("Анна Иванова", "МПИ-101"));
        students.add(new Student("Сергей Петров", "МПИ-101"));
        students.add(new Student("Мария Смирнова", "МПИ-102"));

        StudentAdapter adapter = new StudentAdapter(students, student -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("name", student.getName());
            intent.putExtra("group", student.getGroup());
```

---

## 5. Практика: список студентов · продолжение

```java
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
```

В этом базовом варианте экран деталей только открывается. Если по заданию нужно вернуть результат, замените `startActivity(intent)` на `detailLauncher.launch(intent)` и добавьте поле `ActivityResultLauncher<Intent>` из раздела «Возврат результата с экрана».

---

## 5. Практика: список студентов · продолжение

`activity_detail.xml`:

---

## 5. Практика: список студентов · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="24sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/groupTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
```

---

## 5. Практика: список студентов · продолжение

```xml
        android:layout_marginTop="12dp"
        android:textSize="18sp" />

</LinearLayout>
```

`DetailActivity.java`:

---

## 5. Практика: список студентов · продолжение

```java
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView groupTextView = findViewById(R.id.groupTextView);

        String name = getIntent().getStringExtra("name");
        String group = getIntent().getStringExtra("group");

        nameTextView.setText(name);
        groupTextView.setText(group);
    }
}
```

---

## Типичные ошибки

- забыта зависимость RecyclerView;
- в `onCreateViewHolder()` указан неправильный layout;
- `getItemCount()` возвращает 0;
- в манифест не добавлена новая Activity;
- данные переданы с одним ключом, а читаются с другим;
- результат из второго экрана не возвращается или обрабатывается до регистрации `ActivityResultLauncher`.

---

## Контрольные вопросы

1. Что делает Adapter в RecyclerView?
2. Зачем нужен ViewHolder?
3. Чем `LinearLayout` отличается от `ConstraintLayout`?
4. Как передать данные из одной Activity в другую и вернуть результат обратно?
5. В каких случаях удобно использовать Fragment?

---

## Домашнее задание

Создать приложение «Список студентов»:

- первый экран показывает список минимум из 10 студентов;
- каждая строка содержит имя и группу;
- при нажатии открывается экран деталей;
- экран деталей показывает имя, группу, email и средний балл;
- экран деталей возвращает имя выбранного студента на первый экран через Activity Result API;
- добавить кнопку «Назад» или использовать системную навигацию.
