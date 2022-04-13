package com.redeyesncode.pickmeredeyesncode.adapter;

import android.net.Uri;

public class Image {

    private final Uri uri;
    private final String name;
    private final int size;

    public Image(Uri uri, String name, int size) {
        this.uri = uri;
        this.name = name;
        this.size = size;
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
}
