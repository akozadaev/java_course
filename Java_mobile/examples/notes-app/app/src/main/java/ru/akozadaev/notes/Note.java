package ru.akozadaev.notes;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity public class Note {
    @PrimaryKey(autoGenerate=true) public long id;
    public String title, text; public long createdAt;
    public Note(String title, String text, long createdAt) { this.title=title; this.text=text; this.createdAt=createdAt; }
}
