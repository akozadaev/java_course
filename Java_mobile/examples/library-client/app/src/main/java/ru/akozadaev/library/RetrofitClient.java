package ru.akozadaev.library;
import retrofit2.Retrofit; import retrofit2.converter.gson.GsonConverterFactory;
public final class RetrofitClient {
    private static final LibraryApi API=new Retrofit.Builder().baseUrl("https://gutendex.com/").addConverterFactory(GsonConverterFactory.create()).build().create(LibraryApi.class);
    private RetrofitClient(){} public static LibraryApi api(){return API;}
}
