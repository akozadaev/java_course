package ru.akozadaev.students;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Holder> {
    public interface Listener { void onClick(Student student); }
    private final List<Student> items;
    private final Listener listener;
    public StudentAdapter(List<Student> items, Listener listener) { this.items = items; this.listener = listener; }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
    }
    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        Student item = items.get(position);
        holder.name.setText(item.name); holder.group.setText(item.group);
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }
    @Override public int getItemCount() { return items.size(); }
    static class Holder extends RecyclerView.ViewHolder {
        final TextView name, group;
        Holder(View view) { super(view); name = view.findViewById(R.id.nameTextView); group = view.findViewById(R.id.groupTextView); }
    }
}
