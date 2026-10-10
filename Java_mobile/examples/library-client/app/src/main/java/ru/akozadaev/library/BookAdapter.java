package ru.akozadaev.library;
import android.view.*; import android.widget.TextView; import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView; import java.util.*;
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder>{
    private final List<Book> items=new ArrayList<>(); public void submit(List<Book> books){items.clear();items.addAll(books);notifyDataSetChanged();}
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p,int t){return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_book,p,false));}
    @Override public void onBindViewHolder(@NonNull Holder h,int p){Book b=items.get(p);h.title.setText(b.title);h.author.setText(b.authorNames());}
    @Override public int getItemCount(){return items.size();}
    static class Holder extends RecyclerView.ViewHolder{final TextView title,author;Holder(View v){super(v);title=v.findViewById(R.id.titleTextView);author=v.findViewById(R.id.authorTextView);}}
}
