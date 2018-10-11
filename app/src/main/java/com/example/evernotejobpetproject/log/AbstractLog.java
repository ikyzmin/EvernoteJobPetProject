package com.example.evernotejobpetproject.log;

public abstract class AbstractLog {
    public abstract void d(String message);

    public abstract void d(String tag, String message);

    public abstract void d(String message, String... args);

    public abstract void d(String tag, String message, String... args);

    public abstract void e(String message);

    public abstract void e(String tag, String message);

    public abstract void e(Throwable throwable, String message);

    public abstract void e(String tag, Throwable throwable, String message);

    public abstract void e(Throwable throwable, String message, String... args);

    public abstract void e(String tag, Throwable throwable, String message, String... args);
}
