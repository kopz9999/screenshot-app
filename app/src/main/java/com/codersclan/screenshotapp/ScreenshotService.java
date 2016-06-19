package com.codersclan.screenshotapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.backup.FileBackupHelper;
import android.content.Intent;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by kopz9 on 6/18/2016.
 */
public class ScreenshotService extends Service {
    public final String TAG = "DEBUG";
    public static Boolean serviceRunning = false;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private String pathToWatch;
    private FileObserver observer;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            // stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        // mServiceLooper = thread.getLooper();
        // mServiceHandler = new ServiceHandler(mServiceLooper);
        this.watchScreenshots();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        // Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        // Message msg = mServiceHandler.obtainMessage();
        // msg.arg1 = startId;
        // mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private void watchScreenshots() {
        this.pathToWatch = android.os.Environment.getExternalStorageDirectory()
                + File.separator + android.os.Environment.DIRECTORY_PICTURES
                + File.separator + "Screenshots";;
        this.listenForFileChanges();
    }

    private void listenForFileChanges() {
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

    public void processFile(String file) {
        String fullPath = this.pathToWatch + File.separator + file;
        Log.d(TAG, "File created [" + fullPath + "]");
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.putExtra("imagePath", fullPath);
        startIntent.setFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        this.startActivity(startIntent);
    }
}
