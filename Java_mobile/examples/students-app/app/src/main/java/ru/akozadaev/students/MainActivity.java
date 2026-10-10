package ru.akozadaev.students;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Student> students = Arrays.asList(
        new Student("Анна Иванова", "МПИ-101", "anna@example.ru", 4.8), new Student("Сергей Петров", "МПИ-101", "sergey@example.ru", 4.2),
        new Student("Мария Смирнова", "МПИ-102", "maria@example.ru", 4.9), new Student("Илья Волков", "МПИ-102", "ilya@example.ru", 3.9),
        new Student("Ольга Соколова", "МПИ-103", "olga@example.ru", 4.6), new Student("Денис Морозов", "МПИ-103", "denis@example.ru", 4.1),
        new Student("Елена Новикова", "МПИ-104", "elena@example.ru", 4.7), new Student("Павел Фёдоров", "МПИ-104", "pavel@example.ru", 3.8),
        new Student("Дарья Кузнецова", "МПИ-105", "daria@example.ru", 4.5), new Student("Алексей Орлов", "МПИ-105", "alexey@example.ru", 4.3));

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); setContentView(R.layout.activity_main);
        TextView resultView = findViewById(R.id.resultTextView);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null)
                resultView.setText(getString(R.string.selected, result.getData().getStringExtra("name")));
        });
        RecyclerView list = findViewById(R.id.studentsRecyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new StudentAdapter(students, student -> launcher.launch(DetailActivity.intent(this, student))));
    }
}
