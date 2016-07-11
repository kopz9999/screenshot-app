package com.codersclan.screenshotapp;

import android.app.Application;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;

/**
 * Created by kopz9 on 7/9/2016.
 */
/* 1) Implement IAviaryClientCredentials when using the Image Editor */
public class MainApplication extends Application implements IAviaryClientCredentials {
    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "0f2a2cc1ce184cf3a16401efc7bb162d";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "7ed917ae-1023-4264-b361-1c9d2c07e3b3";

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    /* 2) Add the getBillingKey() method */
    @Override
    public String getBillingKey() {
        return ""; // Leave this blank
    }
}
