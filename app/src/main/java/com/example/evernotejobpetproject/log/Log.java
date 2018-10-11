package com.example.evernotejobpetproject.log;

public class Log extends AbstractLog {

    public static String TAG = Log.class.getSimpleName();

    @Override
    public void d(String message) {
        android.util.Log.d(TAG, message);
    }

    public void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    @Override
    public void d(String message, String... args) {
        android.util.Log.d(TAG, String.format(message, args));
    }

    @Override
    public void d(String tag, String message, String... args) {
        android.util.Log.d(tag, String.format(message, args));
    }


    @Override
    public void e(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    @Override
    public void e(String message) {
        android.util.Log.e(TAG, message);
    }

    @Override
    public void e(String tag, Throwable throwable, String message) {
        android.util.Log.e(tag, message, throwable);
    }

    @Override
    public void e(Throwable throwable, String message) {
        android.util.Log.e(TAG, message, throwable);
    }

    @Override
    public void e(String tag, Throwable throwable, String message, String... args) {
        android.util.Log.e(tag, String.format(message, args), throwable);
    }

    @Override
    public void e(Throwable throwable, String message, String... args) {
        android.util.Log.e(TAG, String.format(message, args), throwable);
    }
}
