package ru.akozadaev.students;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    static Intent intent(Context context, Student s) {
        return new Intent(context, DetailActivity.class).putExtra("name", s.name).putExtra("group", s.group).putExtra("email", s.email).putExtra("grade", s.grade);
    }
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); setContentView(R.layout.activity_detail);
        Intent data = getIntent(); String name = data.getStringExtra("name");
        ((TextView)findViewById(R.id.nameTextView)).setText(name);
        ((TextView)findViewById(R.id.groupTextView)).setText(data.getStringExtra("group"));
        ((TextView)findViewById(R.id.emailTextView)).setText(data.getStringExtra("email"));
        ((TextView)findViewById(R.id.gradeTextView)).setText(String.format(Locale.getDefault(), "%.1f", data.getDoubleExtra("grade", 0)));
        findViewById(R.id.selectButton).setOnClickListener(v -> { setResult(RESULT_OK, new Intent().putExtra("name", name)); finish(); });
    }
}
