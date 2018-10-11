package com.example.evernotejobpetproject.zip;

import android.content.Context;
import android.support.annotation.CallSuper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class ZipArchive {
    private static final String FILENAME_FORMAT = "logs_archive_%s.zip";
    private SingleSubject<File> zipFileSubject = SingleSubject.create();
    private ZipOutputStream zipOutputStream;
    private FileInputStream fileInputStream;

    public Single<File> getZipFileSingle() {
        return zipFileSubject;
    }


    public void zip(File directory, File... filesToZip) {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_yy", Locale.getDefault());
        File zipFile = new File(directory, String.format(FILENAME_FORMAT, dateFormat.format(new Date())));
        try {

            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File fileToZip : filesToZip) {
                fileInputStream = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOutputStream.putNextEntry(zipEntry);
                byte[] zipToFileBytes = new byte[fileInputStream.available()];
                fileInputStream.read(zipToFileBytes);
                zipOutputStream.write(zipToFileBytes);
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }
            zipFileSubject.onSuccess(zipFile);
        } catch (IOException exception) {
            zipFileSubject.onError(exception);
        } finally {
            try {
                zipOutputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
