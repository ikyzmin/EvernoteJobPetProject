package com.example.evernotejobpetproject.jobs;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobRequest;
import com.example.evernotejobpetproject.App;
import com.example.evernotejobpetproject.R;
import com.example.evernotejobpetproject.jobs.creators.TestJobCreator;
import com.example.evernotejobpetproject.log.FileLog;
import com.example.evernotejobpetproject.log.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.IoScheduler;

public class TestJobRemote extends Job {

    private static final int SYNC_INTERVAL_LEGACY = 5;
    private static final int SYNC_INTERVAL_M = 15;
    private final Log log = new Log();
    private final FileLog fileLog = new FileLog(log);

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        try {
            fileLog.d(TestJobRemote.class.getSimpleName(), "Test job running");

            App.getInstance().getService().getPage()
                    .subscribeOn(new IoScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        System.out.println(response); //Log the result
                        fileLog.d(TestJobRemote.class.getSimpleName(), "response success");
                        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (notificationManager.getNotificationChannel("TEST CHANNEL") == null) {
                                notificationManager.createNotificationChannel(new NotificationChannel("TEST CHANNEL", "TEST CHANNEL", NotificationManager.IMPORTANCE_DEFAULT));
                            }
                        }
                        notificationManager.notify(1, new NotificationCompat.Builder(getContext(), "TEST CHANNEL")
                                .setSmallIcon(R.drawable.ic_warning_black_24dp)
                                .setContentTitle("Request is complete")
                                .setContentText("TEST")
                                .build());

                        fileLog.d(TestJobRemote.class.getSimpleName(), "notification shown");
                        fileLog.close();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            try {
                fileLog.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {

        }
        return Result.SUCCESS;
    }

    @Override
    protected void onCancel() {
        try {
            fileLog.d(TestJobRemote.class.getSimpleName(), "Test job cancel");
        } catch (IOException e) {
            log.e(TestJobRemote.class.getSimpleName(), e, "something wrong");
        } finally {
            try {
                fileLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onCancel();
    }

    public static void scheduleMe() {
        final long syncPeriodTime;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            JobConfig.setAllowSmallerIntervalsForMarshmallow(true);
            syncPeriodTime = SYNC_INTERVAL_LEGACY;
        } else {
            syncPeriodTime = SYNC_INTERVAL_M;
        }

        new JobRequest.Builder(TestJobCreator.TEST_PERIODIC_JOB_REMOTE_TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(syncPeriodTime))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
