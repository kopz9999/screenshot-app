package com.codersclan.screenshotapp;

import android.app.IntentService;
import android.app.backup.FileBackupHelper;
import android.content.Intent;
import android.os.FileObserver;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by kopz9 on 6/18/2016.
 */
public class ScreenshotService extends IntentService {
    public final String TAG = "DEBUG";
    public static Boolean serviceRunning = false;
    private String pathToWatch;
    public static FileObserver observer;

    public ScreenshotService() {
        super("ScreenshotService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.watchScreenshots();
        observer = new FileObserver(this.pathToWatch) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                if (event == FileObserver.CREATE) {
                    processFile(file);
                }
            }
        };
        observer.startWatching();
    }

    private void watchCamera() {
        this.pathToWatch = android.os.Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
    }

    private void watchScreenshots() {
        this.pathToWatch = android.os.Environment.getExternalStorageDirectory()
                + File.separator + android.os.Environment.DIRECTORY_PICTURES
                + File.separator + "Screenshots";;
    }

    public void processFile(String file) {
        String fullPath = this.pathToWatch + File.separator + file;
        Log.d(TAG, "File created [" + fullPath + "]");
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.putExtra("imagePath", fullPath);
        startIntent.setFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );
        this.startActivity(startIntent);
    }
}
