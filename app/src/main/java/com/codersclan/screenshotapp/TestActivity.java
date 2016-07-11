package com.codersclan.screenshotapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.adobe.creativesdk.aviary.AdobeImageIntent;

/**
 * Created by kopz9 on 6/28/2016.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.i("INFO", "Example1!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

    /* 1) Make a new Uri object (Replace this with a real image on your device) */
        // Uri imageUri = Uri.parse("content://media/external/images/media/####");
        // Uri imageUri = Uri.parse("android.resource://com.codersclan.screenshotapp/drawable/test");
        Uri imageUri = Uri.parse("http://placekitten.com.s3.amazonaws.com/homepage-samples/408/287.jpg");

        // Log.i("INFO", "Example!!!!");

    /* 2) Create a new Intent */
        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
                .setData(imageUri)
                .build();

    /* 3) Start the Image Editor with request code 1 */
        startActivityForResult(imageEditorIntent, 1);
    }
}
