package ru.akozadaev.library;
import retrofit2.Call; import retrofit2.http.GET; import retrofit2.http.Query;
public interface LibraryApi { @GET("books") Call<BooksResponse> books(@Query("search") String search); }
