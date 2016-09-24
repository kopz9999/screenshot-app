package com.codersclan.screenshotapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.codersclan.screenshotapp.utils.AllowedApps;
import com.codersclan.screenshotapp.utils.FileManager;
import com.codersclan.screenshotapp.utils.Helper;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {
    /* TODO: Consider Removing */
    private String currentImagePath;
    public final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setImage();
        // this.initImagePath( "/sdcard/test.jpg" );
    }

    public void onCropClick(View view) {
        this.launchEditorWithTools(new ToolLoaderFactory.Tools[]{ToolLoaderFactory.Tools.CROP});
    }

    public void onDrawClick(View view) {
        this.launchEditorWithTools(new ToolLoaderFactory.Tools[]{ToolLoaderFactory.Tools.DRAW});
    }

    public void onMemeClick(View view) {
        this.launchEditorWithTools(new ToolLoaderFactory.Tools[]{ToolLoaderFactory.Tools.MEME});
    }

    public void onTextClick(View view) {
        this.launchEditorWithTools(new ToolLoaderFactory.Tools[]{ToolLoaderFactory.Tools.TEXT});
    }

    /* TODO: Remove */
    public void onEditClick(View view) {
        Uri uri =  Uri.fromFile(new File(this.currentImagePath));
        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
                .setData(uri)
                .build();
        startActivityForResult(imageEditorIntent, 1);
    }

    public void onShareClick(View view) {
        List<Intent> intentShareList = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resolveInfoList =
            getPackageManager().queryIntentActivities(shareIntent, 0);

        for(AllowedApps allowedApp : Helper.orderedPackages) {
            for (ResolveInfo resInfo : resolveInfoList) {
                String packageName = resInfo.activityInfo.packageName;
                String name = resInfo.activityInfo.name;
                Log.d(TAG, "Package Name : " + packageName);
                Log.d(TAG, "Name : " + name);

                if (packageName.contains(allowedApp.toString())) {
                    Intent intent = this.getCurrentImageIntent();
                    intent.setComponent(new ComponentName(packageName, name));
                    intentShareList.add(intent);
                }
            }
        }

        if (intentShareList.isEmpty()) {
            Toast.makeText(MainActivity.this, "No apps to share !", Toast.LENGTH_SHORT).show();
        } else {
            Intent chooserIntent =
                    Intent.createChooser(intentShareList.remove(intentShareList.size() - 1), "Share via");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentShareList.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        }
    }

    private Intent getCurrentImageIntent() {
        Uri uri =  Uri.fromFile(new File(this.currentImagePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        return shareIntent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    this.processAdobeImageEditorResult(data.getData());
                    break;
            }
        }
    }

    private void launchEditorWithTools(ToolLoaderFactory.Tools[] mTools) {
        Uri uri =  Uri.fromFile(new File(this.currentImagePath));
        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
                .setData(uri)
                .withToolList(mTools)
                .build();
        startActivityForResult(imageEditorIntent, 1);
    }

    /* NOTE: Gear Logic Begins here */

    // Setup image from Uri
    private void processImageUri(Uri imageUri) {
        final ImageView targetImageView = (ImageView) findViewById(R.id.targetImageView);
        try {
            Drawable drawable = null;
            do {
                URL myUrl = new URL(imageUri.toString());
                InputStream inputStream = (InputStream)myUrl.getContent();
                drawable = Drawable.createFromStream(inputStream, null);
            } while (drawable == null);
            targetImageView.setImageDrawable(drawable);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void processAdobeImageEditorResult(Uri resultUri) {
        this.currentImagePath = resultUri.getPath();
        this.processImageUri(resultUri);
    }

    private void setImage() {
        Bundle b = getIntent().getExtras();
        if (b == null) return;
        if (!b.containsKey("imagePath")) return;
        this.initImagePath(b.getString("imagePath"));
    }

    private void initImagePath(String imagePath) {
        this.currentImagePath = imagePath;
        this.processImageUri( Uri.fromFile(new File(this.currentImagePath)) );
    }

    private void verifyService() {
        Intent mServiceIntent;
        if (ScreenshotService.serviceRunning) return;
        mServiceIntent = new Intent(this, ScreenshotService.class);
        this.startService(mServiceIntent);
    }

}
