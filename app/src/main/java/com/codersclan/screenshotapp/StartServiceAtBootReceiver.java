package com.codersclan.screenshotapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kopz9 on 6/18/2016.
 */
public class StartServiceAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceLauncher = new Intent(context, ScreenshotService.class);
        context.startService(serviceLauncher);
        Log.v("DEBUG", "Service loaded at start");
    }
}
