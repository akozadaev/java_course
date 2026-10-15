package ru.akozadaev.library;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import java.io.IOException;

public final class RetrofitClient {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                if (BuildConfig.RAPIDAPI_KEY.isEmpty()) {
                    throw new IOException("Укажите rapidapi.key в local.properties и пересоберите приложение");
                }
                return chain.proceed(chain.request().newBuilder()
                        .header("X-RapidAPI-Key", BuildConfig.RAPIDAPI_KEY)
                        .header("X-RapidAPI-Host", "project-gutenberg-free-books-api1.p.rapidapi.com")
                        .build());
            })
            .build();
    private static final LibraryApi API = new Retrofit.Builder()
            .baseUrl("https://project-gutenberg-free-books-api1.p.rapidapi.com/")
            .client(CLIENT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LibraryApi.class);

    private RetrofitClient() {
    }

    public static LibraryApi api() {
        return API;
    }
}
