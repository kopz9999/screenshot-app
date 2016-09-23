package com.codersclan.screenshotapp.utils;

/**
 * Created by kopz9 on 9/23/2016.
 */

public enum AllowedApps {
    MESSAGE {
        public String toString() {
            return "com.android.mms";
        }
    },
    EMAIL {
        public String toString() {
            return "com.google.android.gm";
        }
    },
    FACEBOOK {
        public String toString() {
            return "com.facebook";
        }
    },
    INSTAGRAM {
        public String toString() {
            return "com.instagram.android";
        }
    },
    SNAPCHAT {
        public String toString() {
            return "com.snapchat.android";
        }
    },
    PINTEREST {
        public String toString() {
            return "com.pinterest";
        }
    },
}
