package ru.akozadaev.notes;
import androidx.room.*;
import java.util.List;
@Dao public interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY createdAt DESC") List<Note> newestFirst();
    @Query("SELECT * FROM Note ORDER BY createdAt ASC") List<Note> oldestFirst();
    @Insert void insert(Note note);
    @Delete void delete(Note note);
}
