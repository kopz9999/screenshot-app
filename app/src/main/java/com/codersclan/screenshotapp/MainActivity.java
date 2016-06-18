package com.codersclan.screenshotapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setImage();
        this.verifyService();
    }

    public void onCropClick(View view) {
        Uri uri =  Uri.fromFile(new File(this.currentImagePath));
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                this.processImageUri(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // Setup image from Uri
    private void processImageUri(Uri imageUri) {
        ImageView targetImageView = (ImageView) findViewById(R.id.targetImageView);
        Picasso.with(this).load(imageUri).into(targetImageView);
    }

    private void setImage() {
        Bundle b = getIntent().getExtras();
        if (b == null) return;
        if (!b.containsKey("imagePath")) return;
        this.currentImagePath = b.getString("imagePath");
        this.processImageUri( Uri.fromFile(new File(this.currentImagePath)) );
    }

    private void verifyService() {
        Intent mServiceIntent;
        if (ScreenshotService.serviceRunning) return;
        mServiceIntent = new Intent(this, ScreenshotService.class);
        this.startService(mServiceIntent);
    }

}
