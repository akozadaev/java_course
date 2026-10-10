package ru.akozadaev.notes;
import android.content.SharedPreferences; import android.os.Bundle; import android.widget.*;
import androidx.appcompat.app.*; import androidx.recyclerview.widget.*; import androidx.room.Room;
import java.util.concurrent.*;
public class MainActivity extends AppCompatActivity {
    private AppDatabase db; private ExecutorService executor; private NoteAdapter adapter; private boolean newestFirst;
    @Override protected void onCreate(Bundle state){super.onCreate(state);setContentView(R.layout.activity_main);
        db=Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"notes.db").build();executor=Executors.newSingleThreadExecutor();
        SharedPreferences prefs=getSharedPreferences("settings",MODE_PRIVATE);newestFirst=prefs.getBoolean("newest_first",true);
        Switch sort=findViewById(R.id.sortSwitch);sort.setChecked(newestFirst);sort.setOnCheckedChangeListener((b,value)->{newestFirst=value;prefs.edit().putBoolean("newest_first",value).apply();load();});
        EditText title=findViewById(R.id.titleEditText),text=findViewById(R.id.textEditText);
        adapter=new NoteAdapter(note->executor.execute(()->{db.noteDao().delete(note);load();}));RecyclerView list=findViewById(R.id.notesRecyclerView);list.setLayoutManager(new LinearLayoutManager(this));list.setAdapter(adapter);
        findViewById(R.id.saveButton).setOnClickListener(v->{String t=title.getText().toString().trim();if(t.isEmpty()){title.setError(getString(R.string.enter_title));return;}Note note=new Note(t,text.getText().toString().trim(),System.currentTimeMillis());executor.execute(()->{db.noteDao().insert(note);runOnUiThread(()->{title.setText("");text.setText("");});load();});});load();}
    private void load(){executor.execute(()->{var notes=newestFirst?db.noteDao().newestFirst():db.noteDao().oldestFirst();runOnUiThread(()->adapter.submit(notes));});}
    @Override protected void onDestroy(){super.onDestroy();executor.shutdown();}
}
