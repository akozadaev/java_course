package ru.akozadaev.notes;
import android.view.*; import android.widget.TextView;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.*;
import java.text.DateFormat; import java.util.*;
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Holder> {
    public interface Listener { void onLongClick(Note note); }
    private final List<Note> items=new ArrayList<>(); private final Listener listener;
    public NoteAdapter(Listener listener){this.listener=listener;}
    public void submit(List<Note> notes){items.clear();items.addAll(notes);notifyDataSetChanged();}
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p,int t){return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_note,p,false));}
    @Override public void onBindViewHolder(@NonNull Holder h,int p){Note n=items.get(p);h.title.setText(n.title);h.text.setText(n.text);h.date.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(new Date(n.createdAt)));h.itemView.setOnLongClickListener(v->{listener.onLongClick(n);return true;});}
    @Override public int getItemCount(){return items.size();}
    static class Holder extends RecyclerView.ViewHolder{final TextView title,text,date;Holder(View v){super(v);title=v.findViewById(R.id.titleTextView);text=v.findViewById(R.id.textTextView);date=v.findViewById(R.id.dateTextView);}}
}
