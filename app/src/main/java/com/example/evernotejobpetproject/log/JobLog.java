package com.example.evernotejobpetproject.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.util.JobLogger;

import java.io.IOException;

public class JobLog implements JobLogger {

    private FileLog fileLog = new FileLog(new Log());

    @Override
    public void log(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable t) {
        try {
            if (priority != android.util.Log.INFO) {
                fileLog.d(tag, message);
            } else {
                fileLog.e(tag, t, message);
            }
            fileLog.write();
        } catch (IOException exception) {
            exception.printStackTrace();
            try {
                fileLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
