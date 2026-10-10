package ru.akozadaev.library;
import android.os.Bundle; import android.view.View; import android.widget.*; import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.*; import retrofit2.*;
public class MainActivity extends AppCompatActivity {
    private BookAdapter adapter; private ProgressBar progress; private TextView message; private RecyclerView list;
    @Override protected void onCreate(Bundle state){super.onCreate(state);setContentView(R.layout.activity_main);EditText query=findViewById(R.id.searchEditText);progress=findViewById(R.id.progressBar);message=findViewById(R.id.messageTextView);list=findViewById(R.id.booksRecyclerView);adapter=new BookAdapter();list.setLayoutManager(new LinearLayoutManager(this));list.setAdapter(adapter);findViewById(R.id.searchButton).setOnClickListener(v->load(query.getText().toString().trim()));load("");}
    private void load(String query){showLoading();RetrofitClient.api().books(query).enqueue(new Callback<>(){
        @Override public void onResponse(Call<BooksResponse> call,Response<BooksResponse> response){if(response.isSuccessful()&&response.body()!=null){var books=response.body().results;adapter.submit(books);if(books.isEmpty())showMessage(getString(R.string.empty));else showList();}else showMessage(getString(R.string.server_error,response.code()));}
        @Override public void onFailure(Call<BooksResponse> call,Throwable error){showMessage(getString(R.string.network_error,error.getLocalizedMessage()));}
    });}
    private void showLoading(){progress.setVisibility(View.VISIBLE);message.setVisibility(View.GONE);list.setVisibility(View.GONE);}
    private void showList(){progress.setVisibility(View.GONE);message.setVisibility(View.GONE);list.setVisibility(View.VISIBLE);}
    private void showMessage(String text){progress.setVisibility(View.GONE);list.setVisibility(View.GONE);message.setText(text);message.setVisibility(View.VISIBLE);}
}
