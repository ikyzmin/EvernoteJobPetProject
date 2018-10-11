package com.example.evernotejobpetproject.log;

import android.support.annotation.CallSuper;

import java.io.File;
import java.io.IOException;

public abstract class WriteableLog {

    public abstract void d(String message) throws IOException;

    public abstract void d(String tag, String message) throws IOException;

    public abstract void d(String message, String... args) throws IOException;

    public abstract void d(String tag, String message, String... args) throws IOException;

    public abstract void e(String message) throws IOException;

    public abstract void e(String tag, String message) throws IOException;

    public abstract void e(Throwable throwable, String message) throws IOException;

    public abstract void e(String tag, Throwable throwable, String message) throws IOException;

    public abstract void e(Throwable throwable, String message, String... args) throws IOException;

    public abstract void e(String tag, Throwable throwable, String message, String... args) throws IOException;

    public abstract File logDirectory();

    public abstract String logFilesPattern();

    public abstract void close() throws IOException;
}
