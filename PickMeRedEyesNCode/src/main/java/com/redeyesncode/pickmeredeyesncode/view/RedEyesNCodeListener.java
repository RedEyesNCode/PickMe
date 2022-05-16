package com.redeyesncode.pickmeredeyesncode.view;

import android.graphics.Bitmap;
import android.net.Uri;

public interface RedEyesNCodeListener {
    void onImageReceive(Uri uri);
    void onVideoReceive(Uri uri);
    void onBitmapReceive(Bitmap bitmap);
}
