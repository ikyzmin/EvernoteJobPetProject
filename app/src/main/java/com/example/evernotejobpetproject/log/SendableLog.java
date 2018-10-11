package com.example.evernotejobpetproject.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import io.reactivex.subjects.SingleSubject;

public class SendableLog extends WriteableLog {

    private WriteableLog origin;
    private SingleSubject<File[]> logsSingleSubject = SingleSubject.create();

    public SendableLog(WriteableLog origin) {
        this.origin = origin;
    }

    @Override
    public void d(String message) throws IOException {
        origin.d(message);
    }

    @Override
    public void d(String tag, String message) throws IOException {
        origin.d(tag, message);
    }

    @Override
    public void d(String message, String... args) throws IOException {
        origin.d(message, args);
    }

    @Override
    public void d(String tag, String message, String... args) throws IOException {
        origin.d(tag, message, args);
    }

    @Override
    public void e(String message) throws IOException {
        origin.e(message);
    }

    @Override
    public void e(String tag, String message) throws IOException {
        origin.e(tag, message);
    }

    @Override
    public void e(Throwable throwable, String message) throws IOException {
        origin.e(throwable, message);
    }

    @Override
    public void e(String tag, Throwable throwable, String message) throws IOException {
        origin.e(tag, throwable, message);
    }

    @Override
    public void e(Throwable throwable, String message, String... args) throws IOException {
        origin.e(throwable, message, args);
    }

    @Override
    public void e(String tag, Throwable throwable, String message, String... args) throws IOException {
        origin.e(tag, throwable, message, args);
    }

    @Override
    public File logDirectory() {
        return origin.logDirectory();
    }

    @Override
    public String logFilesPattern() {
        return origin.logFilesPattern();
    }

    @Override
    public void close() throws IOException {
        origin.close();
    }

    @Override
    public void write() throws IOException {
        origin.write();
    }

    public SingleSubject<File[]> getLogs() {
        return logsSingleSubject;
    }

    public void logs() {
        ArrayList<File> fileList = new ArrayList<>();
        File logDirectory = logDirectory();
        String logPattern = logFilesPattern();
        Pattern pattern = Pattern.compile(logPattern);
        for (File file : logDirectory.listFiles()) {
            if (pattern.matcher(file.getName()).matches()) {
                fileList.add(file);
            }
        }
        File[] fileArray = new File[fileList.size()];
        logsSingleSubject.onSuccess(fileList.toArray(fileArray));
    }
}
