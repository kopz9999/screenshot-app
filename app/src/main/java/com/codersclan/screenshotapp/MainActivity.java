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

import com.codersclan.screenshotapp.utils.FileManager;
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
                this.processCropResult(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // Setup image from Uri
    private void processImageUri(Uri imageUri) {
        final ImageView targetImageView = (ImageView) findViewById(R.id.targetImageView);
        final Uri imageUriParam = imageUri;
        // targetImageView.postInvalidateDelayed(5000);
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run()
                    {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        targetImageView.setImageDrawable(null); // <--- added to force redraw of ImageView
                        targetImageView.setImageURI(imageUriParam);
                    }
                });
            }
        };
        thread.start();
    }

    private void processCropResult(Uri resultUri) {
        // File myFile = new File(resultUri.toString());
        // this.currentImagePath = myFile.getAbsolutePath();
        String sandbox = android.os.Environment.getExternalStorageDirectory()
                + File.separator + "ScreenshotApp" ;;
        File resultFile = new File(resultUri.getPath());
        File sandboxDirectory = new File(sandbox);
        sandboxDirectory.mkdirs();
        File outputFile = new File(sandboxDirectory, resultFile.getName());
        try {
            FileManager.copyFile(resultFile, outputFile);
            this.currentImagePath = outputFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.processImageUri(resultUri);
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
