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
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCropClick(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        // CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        // cropImageView.setImageBitmap(bitmap);
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/Download";
        File f = new File( extStorageDirectory ) ;
        File list[] = f.listFiles();
        File file = new File(extStorageDirectory, "test.jpg");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // List<String> myList = new ArrayList<String>();

        // String root_sd = Environment.getRootDirectory().toString();
        // String imageUri = "drawable://" + R.drawable.test;
        String imageUri = extStorageDirectory + "/test.jpg";

        // File file = new File( root_sd ) ;
        // File list[] = file.listFiles();
        // String filePath = list[0].getAbsolutePath();

        //
        // Uri uri =  Uri.parse( "http://placekitten.com.s3.amazonaws.com/homepage-samples/200/287.jpg" );
        Uri uri =  Uri.fromFile(new File(imageUri));
        // Uri uri = Uri.parse("android.resource://com.codersclan.screenshotapp/drawable/test.jpg");;
        // CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        // cropImageView.setImageUriAsync(uri);
        // Toast.makeText(this, "test test",
        //         Toast.LENGTH_LONG).show();
        // Uri uri =  Uri.parse( "http://wp.patheos.com.s3.amazonaws.com/blogs/faithwalkers/files/2013/03/bigstock-Test-word-on-white-keyboard-27134336.jpg" );
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void processImageUri(Uri imageUri) {
        ImageView targetImageView = (ImageView) findViewById(R.id.targetImageView);
        Picasso.with(this).load(imageUri).into(targetImageView);
        // targetImageView
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
}
