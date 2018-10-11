package com.example.evernotejobpetproject.remote.server;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface ExampleService {

    @GET("/")
    Single<String> getPage();
}
