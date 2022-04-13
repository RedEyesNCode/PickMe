package com.redeyesncode.pickmeredeyesncode.model;

import android.net.Uri;

public class Image {

    private final Uri uri;
    private final String name;
    private final int size;
    private final String imagePath;
    private final int resourceId;


    public Image(Uri uri, String imagePath,String name, int size, int resourceId) {
        this.uri = uri;
        this.imagePath = imagePath;
        this.name = name;
        this.size = size;
        this.resourceId = resourceId;

    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
