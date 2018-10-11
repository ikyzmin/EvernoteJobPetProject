package com.example.evernotejobpetproject.log;

import com.example.evernotejobpetproject.App;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLog extends WriteableLog {
    private static final String LOG_FORMAT = "%s/%s(%s): %s\r\n";
    private static final String FILENAME_FORMAT = "logs_%s.txt";

    private static final String TAG = FileLog.class.getSimpleName();

    private AbstractLog origin;
    private File logfile;
    private File logDirectory;
    private Writer writer;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    public FileLog(AbstractLog origin) {
        this.origin = origin;
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_yy", Locale.getDefault());
        logDirectory = App.getInstance().getExternalFilesDir(null);
        logfile = new File(logDirectory, String.format(FILENAME_FORMAT, dateFormat.format(new Date())));
        try {
            if (!logfile.exists()) {
                logfile.createNewFile();
            }
            origin.d("log file is %s", logfile.getAbsolutePath());
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile, true), "UTF-8"));
        } catch (IOException ignore) {
        }
    }

    private FileLog() {
        //do nothing
    }

    @Override
    public void d(String message) throws IOException {
        origin.d(message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "D", TAG, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void d(String tag, String message) throws IOException {
        origin.d(tag, message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "D", tag, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void d(String message, String... args) throws IOException {
        origin.d(message, args);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "D", TAG, timeFormat.format(new Date()), String.format(message, args)));
        }
    }

    @Override
    public void d(String tag, String message, String... args) throws IOException {
        origin.d(tag, message, args);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "D", tag, timeFormat.format(new Date()), String.format(message, args)));
        }
    }

    @Override
    public void e(String message) throws IOException {
        origin.e(message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", TAG, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void e(String tag, String message) throws IOException {
        origin.e(tag, message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", tag, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void e(Throwable throwable, String message, String... args) throws IOException {
        origin.e(throwable, message, args);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", TAG, timeFormat.format(new Date()), String.format(message, args)));
        }
    }

    @Override
    public void e(String tag, Throwable throwable, String message, String... args) throws IOException {
        origin.e(tag, throwable, message, args);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", tag, timeFormat.format(new Date()), String.format(message, args)));
        }
    }

    @Override
    public void e(Throwable throwable, String message) throws IOException {
        origin.e(throwable, message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", TAG, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void e(String tag, Throwable throwable, String message) throws IOException {
        origin.e(tag, throwable, message);
        if (writer != null) {
            writer.append(String.format(LOG_FORMAT, "E", tag, timeFormat.format(new Date()), message));
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    public File logDirectory() {
        return logDirectory;
    }

    public String logFilesPattern() {
        return "logs_\\d{2}_\\d{2}_\\d{2}.txt";
    }
}
