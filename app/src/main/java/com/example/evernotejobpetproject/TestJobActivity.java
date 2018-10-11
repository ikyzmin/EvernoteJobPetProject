package com.example.evernotejobpetproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.evernotejobpetproject.log.FileLog;
import com.example.evernotejobpetproject.log.Log;
import com.example.evernotejobpetproject.log.SendableLog;
import com.example.evernotejobpetproject.zip.ZipArchive;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestJobActivity extends AppCompatActivity {

    private static final int SEND_LOG_REQUEST = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);
        Button button = findViewById(R.id.send_log_button);
        SendableLog sendableLog = new SendableLog(new FileLog(new Log()));
        ProgressBar progressBar = findViewById(R.id.zip_progress);
        button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            ZipArchive zipArchive = new ZipArchive();
            sendableLog.logs();
            sendableLog.getLogs()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .flatMap(files -> {
                        zipArchive.zip(sendableLog.logDirectory(), files);
                        return zipArchive.getZipFileSingle();
                    })
                    .subscribe(zipFile -> {
                        button.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("vnd.android.cursor.dir/email");
                        Uri uri = Uri.fromFile(zipFile);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy/HH:MM:ss ", Locale.getDefault());
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ilya.kyzmin@myoffice.team"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, String.format("Logs %s", dateFormat.format(new Date())));
                        intent.putExtra(Intent.EXTRA_TEXT, "Zip file attached");
                        startActivityForResult(Intent.createChooser(intent, ""), SEND_LOG_REQUEST);
                        sendableLog.close();
                    }, throwable -> sendableLog.e(throwable, "cannot prepare zip logs"));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SEND_LOG_REQUEST && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Email sent", Toast.LENGTH_LONG).show();
        }
    }
}
