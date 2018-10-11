package com.example.evernotejobpetproject.jobs.creators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.example.evernotejobpetproject.jobs.TestJobLocal;
import com.example.evernotejobpetproject.jobs.TestJobRemote;
import com.example.evernotejobpetproject.log.FileLog;
import com.example.evernotejobpetproject.log.Log;

import java.io.IOException;

public class TestJobCreator implements JobCreator {

    public static final String TEST_PERIODIC_JOB_REMOTE_TAG = "test_periodic_job_remote";
    public static final String TEST_PERIODIC_JOB_LOCAL_TAG = "test_periodic_job_local";

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        Log log = new Log();
        FileLog fileLog = null;
        try {
            fileLog = new FileLog(log);

            switch (tag) {
                case TEST_PERIODIC_JOB_REMOTE_TAG:
                    fileLog.d(TestJobCreator.class.getSimpleName(), "job with remote created");
                    fileLog.close();
                    return new TestJobRemote();
                case TEST_PERIODIC_JOB_LOCAL_TAG:
                    fileLog.d(TestJobCreator.class.getSimpleName(), "job with local created");
                    fileLog.close();
                    return new TestJobLocal();
                default:
                    return null;
            }
        } catch (IOException e) {
            log.e(TestJobCreator.class.getSimpleName(), e, "JOB NOT CREATED CAUSE OF EXCEPTION");
        } finally {
            try {
                fileLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
