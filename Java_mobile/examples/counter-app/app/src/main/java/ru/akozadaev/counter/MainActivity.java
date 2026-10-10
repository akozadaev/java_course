package ru.akozadaev.counter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String KEY_COUNT = "count";
    private TextView countView;
    private TextView statusView;
    private int count;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        countView = findViewById(R.id.countTextView);
        statusView = findViewById(R.id.statusTextView);
        if (state != null) count = state.getInt(KEY_COUNT, 0);
        findViewById(R.id.incrementButton).setOnClickListener(v -> { count++; render(); });
        findViewById(R.id.decrementButton).setOnClickListener(v -> { if (count > 0) count--; render(); });
        findViewById(R.id.resetButton).setOnClickListener(v -> { count = 0; render(); });
        render();
    }

    private void render() {
        countView.setText(String.valueOf(count));
        int status = count == 0 ? R.string.zero : count < 10 ? R.string.small : R.string.large;
        statusView.setText(status);
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        state.putInt(KEY_COUNT, count);
        super.onSaveInstanceState(state);
    }
}
