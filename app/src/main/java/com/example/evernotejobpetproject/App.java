package com.example.evernotejobpetproject;

import android.app.Application;

import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobManager;
import com.example.evernotejobpetproject.jobs.TestJobLocal;
import com.example.evernotejobpetproject.jobs.TestJobRemote;
import com.example.evernotejobpetproject.jobs.creators.TestJobCreator;
import com.example.evernotejobpetproject.log.FileLog;
import com.example.evernotejobpetproject.log.JobLog;
import com.example.evernotejobpetproject.log.Log;
import com.example.evernotejobpetproject.remote.server.ExampleService;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class App extends Application {

    Retrofit retrofit;
    private static App instance;
    ExampleService service;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FileLog fileLog = new FileLog(new Log());
        try {
            JobConfig.addLogger(new JobLog());
            fileLog.d(App.class.getSimpleName(), "App created");
            JobManager.create(this).addJobCreator(new TestJobCreator());
            fileLog.d(App.class.getSimpleName(), "Job manager created");
            TestJobRemote.scheduleMe();
            fileLog.d(App.class.getSimpleName(), "TestJobRemote scheduled");
            TestJobLocal.scheduleMe();
            fileLog.d(App.class.getSimpleName(), "TestJobLocal scheduled");
            fileLog.write();
        } catch (IOException ioException) {
            try {
                fileLog.e(ioException, "cannot log in file");
                fileLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("https://example.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(ExampleService.class);
    }

    public static App getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ExampleService getService() {
        return service;
    }
}
