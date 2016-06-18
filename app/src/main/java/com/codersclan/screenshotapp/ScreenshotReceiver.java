package com.codersclan.screenshotapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kopz9 on 6/17/2016.
 */
public class ScreenshotReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Log file capture
        Log.i("INFO", "Enter BroadcastReceiver");

        Cursor cursor = context.getContentResolver().query(intent.getData(),
                null, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
        Toast.makeText(context, "New Photo is Saved as : " + imagePath, Toast.LENGTH_LONG)
                .show();

        // Intent start
        Intent startIntent = context
                .getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        startIntent.putExtra("imagePath", imagePath);

        startIntent.setFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );
        context.startActivity(startIntent);

    }
}
