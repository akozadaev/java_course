---
marp: true
theme: mobile-course
paginate: true
footer: «Лекция 4. REST-клиент»
size: 16:9
---

<!-- _class: lead -->
<!-- _paginate: false -->

# Лекция 4. REST-клиент

### Разработка мобильных приложений на Java

---

## О лекции

---

## Цель лекции

Научить студентов взаимодействовать с удаленным сервером через REST API с использованием Retrofit, OkHttp и Gson, а также разобрать структуру REST-клиента на примере библиотечного приложения.

---

## План на 90 минут

| Время | Раздел |
|---:|---|
| 0-15 мин | HTTP, REST и JSON |
| 15-40 мин | Retrofit, OkHttp, Gson |
| 40-65 мин | Асинхронные запросы и обработка ошибок |
| 65-85 мин | Практика: клиент библиотеки |
| 85-90 мин | Итоги и домашнее задание |

---

## 1. Основы сетевого взаимодействия

Большинство мобильных приложений обмениваются данными с сервером. Приложение может:

- загружать список товаров;
- отправлять форму регистрации;
- обновлять профиль пользователя;
- получать новости;
- синхронизировать локальные данные.

HTTP - протокол обмена данными между клиентом и сервером.

Основные методы:

---

## 1. Основы сетевого взаимодействия · продолжение

- `GET` - получить данные;
- `POST` - создать новую запись;
- `PUT` или `PATCH` - изменить запись;
- `DELETE` - удалить запись.

Пример REST API для книг:

---

## 1. Основы сетевого взаимодействия · продолжение

```text
GET    /books          получить список книг
GET    /books/15       получить книгу с id 15
POST   /books          создать книгу
PUT    /books/15       обновить книгу
DELETE /books/15       удалить книгу
```

JSON - текстовый формат обмена данными.

---

## 1. Основы сетевого взаимодействия · продолжение

```json
{
  "id": 1,
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "year": 2018
}
```

В Android сетевые операции нельзя выполнять в UI-потоке, потому что сеть может быть медленной. Если заблокировать главный поток, интерфейс перестанет отвечать.

Для доступа к сети нужно разрешение в `AndroidManifest.xml`:

---

## 1. Основы сетевого взаимодействия · продолжение

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

`INTERNET` разрешает сетевые запросы. `ACCESS_NETWORK_STATE` нужен, если приложение заранее проверяет состояние сети перед запросом. Даже при такой проверке нельзя считать запрос гарантированно успешным: ошибки Retrofit все равно обрабатываются в `onFailure()`.

---

## 2. Retrofit

Retrofit - типобезопасный HTTP-клиент для Android и Java. Разработчик описывает API как Java-интерфейс, а Retrofit создает реализацию.

В современных codelab Google пример REST-клиента обычно дан на Kotlin, Compose, ViewModel и kotlinx.serialization. Для Java-курса сохраняется связка Retrofit + OkHttp + Gson, потому что она широко встречается в Java-проектах и хорошо показывает идею декларативного API-интерфейса, асинхронного запроса и преобразования JSON в объектную модель.

Подключение зависимостей:

---

## 2. Retrofit · продолжение

```gradle
dependencies {
    implementation "com.squareup.retrofit2:retrofit:2.11.0"
    implementation "com.squareup.retrofit2:converter-gson:2.11.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
}
```

Роли библиотек:

---

## 2. Retrofit · продолжение

- Retrofit описывает API и выполняет запросы;
- OkHttp является HTTP-клиентом;
- Gson преобразует JSON в Java-объекты и обратно;
- Logging Interceptor помогает видеть запросы и ответы в Logcat.

---

## 3. Модель данных

```java
public class Book {
    private int id;
    private String title;
    private String author;
    private int year;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

```

---

## 3. Модель данных · продолжение

```java
    public int getYear() {
        return year;
    }
}
```

Gson может заполнять поля модели через reflection, поэтому сеттеры в простом POJO не обязательны. В реальном проекте их можно добавить для явного изменения объекта в коде приложения.

Если имена полей в JSON отличаются от Java-полей, используется `@SerializedName`.

---

## 3. Модель данных · продолжение

```java
@SerializedName("published_year")
private int publishedYear;
```

---

## 4. Интерфейс API

```java
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LibraryApiService {
    @GET("books")
    Call<List<Book>> getBooks();

    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") int id);

    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("title") String title);

```

---

## 4. Интерфейс API · продолжение

```java
    @POST("books")
    Call<Book> createBook(@Body Book book);
}
```

Аннотации:

- `@GET("books")` - GET-запрос по относительному пути;
- `@Path("id")` - подстановка значения в путь;
- `@Query("title")` - query-параметр;
- `@Body` - тело запроса.

---

## 5. Создание Retrofit-клиента

```java
public class RetrofitClient {
    private static final String BASE_URL = "https://example.com/api/";
    private static LibraryApiService apiService;

    public static LibraryApiService getApiService() {
        if (apiService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
```

---

## 5. Создание Retrofit-клиента · продолжение

```java

            apiService = retrofit.create(LibraryApiService.class);
        }

        return apiService;
    }
}
```

---

## 5. Создание Retrofit-клиента · продолжение

Важно: `BASE_URL` должен заканчиваться символом `/`. В промышленном коде создание singleton-клиента стоит делать потокобезопасным, например через `volatile`, `synchronized` или dependency injection. Для учебного примера статического поля достаточно.

---

## 6. Асинхронный запрос

```java
LibraryApiService api = RetrofitClient.getApiService();

api.getBooks().enqueue(new Callback<List<Book>>() {
    @Override
    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
        if (response.isSuccessful() && response.body() != null) {
            List<Book> books = response.body();
            if (books.isEmpty()) {
                showEmptyState();
            } else {
                adapter.setBooks(books);
            }
        } else {
            showError("Ошибка сервера: " + response.code());
        }
    }

    @Override
```

---

## 6. Асинхронный запрос · продолжение

```java
    public void onFailure(Call<List<Book>> call, Throwable t) {
        showError("Ошибка сети: " + t.getMessage());
    }
});
```

`onResponse()` вызывается, когда сервер вернул HTTP-ответ. Даже ответ 404 попадет в `onResponse()`, но `response.isSuccessful()` будет `false`.

`onFailure()` вызывается при сетевой ошибке: нет интернета, таймаут, неправильный адрес сервера.

---

## 7. Состояния интерфейса и обработка ошибок

Хороший REST-клиент должен показывать состояния:

- загрузка;
- данные загружены;
- пустой список;
- ошибка сети;
- ошибка сервера.

Эту часть стоит обязательно связать с Google codelab Get data from the internet: там отдельно подчеркивается необходимость разрешения `INTERNET`, обработки медленного или недоступного интернета и преобразования JSON-ответа в объекты приложения. В Java-примере ниже те же идеи реализуются через `Call.enqueue()` и `Callback`.

---

## 7. Состояния интерфейса и обработка ошибок · продолжение

Если нужно заранее проверить наличие активной сети, можно добавить вспомогательный метод:

```java
private boolean isNetworkAvailable() {
    ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager != null ? manager.getActiveNetworkInfo() : null;
    return networkInfo != null && networkInfo.isConnected();
}
```

---

## 7. Состояния интерфейса и обработка ошибок · продолжение

Для новых проектов стоит учитывать, что `getActiveNetworkInfo()` deprecated начиная с API 29. В актуальном коде лучше использовать `ConnectivityManager.NetworkCallback`, например `registerDefaultNetworkCallback()` или `registerNetworkCallback()`, а для характеристик сети - `NetworkCapabilities`. Вводный пример выше оставлен как простой способ объяснить идею проверки сети в Java.

Простой пример:

---

## 7. Состояния интерфейса и обработка ошибок · продолжение

```java
private void setLoading(boolean loading) {
    progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
    emptyTextView.setVisibility(View.GONE);
}

private void showEmptyState() {
    recyclerView.setVisibility(View.GONE);
    emptyTextView.setVisibility(View.VISIBLE);
}

private void showError(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}
```

---

## 8. Практика: клиент библиотеки

Минимальный экран:

- `ProgressBar`;
- `RecyclerView`;
- `EditText` для поиска;
- кнопка «Найти».

`activity_books.xml`:

---

## 8. Практика: клиент библиотеки · продолжение

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/searchEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Название книги" />

    <Button
        android:id="@+id/searchButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
```

---

## 8. Практика: клиент библиотеки · продолжение

```xml
        android:text="Найти" />

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_marginTop="16dp"
        android:visibility="gone" />

    <TextView
        android:id="@+id/emptyTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:gravity="center"
        android:text="Книги не найдены"
        android:visibility="gone" />
```

---

## 8. Практика: клиент библиотеки · продолжение

```xml

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/booksRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginTop="12dp"
        android:layout_weight="1" />

</LinearLayout>
```

---

## 8. Практика: клиент библиотеки · продолжение

Разметка элемента списка `item_book.xml` строится по той же идее, что `item_student.xml`:

---

## 8. Практика: клиент библиотеки · продолжение

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
        android:id="@+id/authorTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
```

---

## 8. Практика: клиент библиотеки · продолжение

```xml
        android:layout_marginTop="4dp"
        android:textSize="14sp" />
</LinearLayout>
```

Минимальный `BookAdapter` аналогичен `StudentAdapter`:

---

## 8. Практика: клиент библиотеки · продолжение

```java
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final List<Book> books;
    private final OnBookClickListener listener;

    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = new ArrayList<>(books);
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
```

---

## 8. Практика: клиент библиотеки · продолжение

```java
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.itemView.setOnClickListener(view -> listener.onBookClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> newBooks) {
        books.clear();
```

---

## 8. Практика: клиент библиотеки · продолжение

```java
        books.addAll(newBooks);
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
        }
    }
}
```

---

## 8. Практика: клиент библиотеки · продолжение

Для самостоятельной доработки можно заменить `notifyDataSetChanged()` на `DiffUtil` по аналогии с `NoteAdapter` из лекции 3.

Фрагмент Activity:

---

## 8. Практика: клиент библиотеки · продолжение

```java
public class BooksActivity extends AppCompatActivity {
    private BookAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private LibraryApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.booksRecyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);

```

---

## 8. Практика: клиент библиотеки · продолжение

```java
        api = RetrofitClient.getApiService();
        adapter = new BookAdapter(new ArrayList<>(), book -> {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(view -> {
            String title = searchEditText.getText().toString();
            searchBooks(title);
        });

        loadBooks();
    }

```

---

## 8. Практика: клиент библиотеки · продолжение

```java
    private void loadBooks() {
        setLoading(true);
        api.getBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    if (books.isEmpty()) {
                        showEmptyState();
                    } else {
                        adapter.setBooks(books);
                    }
                } else {
                    showError("Ошибка сервера: " + response.code());
                }
            }

```

---

## 8. Практика: клиент библиотеки · продолжение

```java
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                setLoading(false);
                showError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void searchBooks(String title) {
        setLoading(true);
        api.searchBooks(title).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();
                    if (books.isEmpty()) {
                        showEmptyState();
```

---

## 8. Практика: клиент библиотеки · продолжение

```java
                    } else {
                        adapter.setBooks(books);
                    }
                } else {
                    showError("Ошибка сервера: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                setLoading(false);
                showError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
```

---

## 8. Практика: клиент библиотеки · продолжение

```java
        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
```

---

## 9. Разбор librarySpringMobileClient

При разборе реального репозитория предложите студентам найти:

- пакет с моделями данных;
- класс или интерфейс API;
- место создания Retrofit;
- Activity или Fragment, где выполняется запрос;
- адаптер RecyclerView;
- обработку ошибок.

Вопросы для анализа кода:

---

## 9. Разбор librarySpringMobileClient · продолжение

1. Где задан базовый URL сервера?
2. Какие endpoint используются?
3. Какие классы соответствуют JSON-ответам?
4. Где вызывается `enqueue()`?
5. Что происходит при ошибке сети?
6. Как данные из ответа попадают в интерфейс?

Типовой паттерн проекта:

---

## 9. Разбор librarySpringMobileClient · продолжение

```text
data/
  model/
    Book.java
  remote/
    LibraryApiService.java
    RetrofitClient.java
ui/
  books/
    BooksActivity.java
    BookAdapter.java
```

---

## Типичные ошибки

- нет разрешения `INTERNET` или, при проверке состояния сети, `ACCESS_NETWORK_STATE`;
- `BASE_URL` не заканчивается `/`;
- сетевой запрос выполняется синхронно в UI-потоке;
- поля модели не совпадают с JSON;
- ошибки сервера и сети не различаются;
- пустой список не обрабатывается.

---

## Контрольные вопросы

1. Что такое REST API?
2. Чем `GET` отличается от `POST`?
3. Для чего нужен Gson?
4. Чем `onResponse()` отличается от `onFailure()`?
5. Почему сетевые операции нельзя выполнять в UI-потоке?

---

## Домашнее задание

Доработать REST-клиент библиотеки:

- добавить экран детальной информации о книге;
- передавать id книги на экран деталей;
- загружать книгу по id через Retrofit;
- добавить поиск по названию;
- показать пользователю сообщение при пустом результате поиска;
- обработать отсутствие интернета;
- кэшировать последний успешно загруженный список книг в Room и показывать его при ошибке сети.
