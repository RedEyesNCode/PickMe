package com.redeyesncode.pickmeredeyesncode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.redeyesncode.pickmeredeyesncode.adapter.GalleryImageAdapter;
import com.redeyesncode.pickmeredeyesncode.adapter.GalleryVideoAdapter;
import com.redeyesncode.pickmeredeyesncode.adapter.Image;
import com.redeyesncode.pickmeredeyesncode.adapter.Video;
import com.redeyesncode.pickmeredeyesncode.databinding.ActivityPickImageFromGalleryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PickImageFromGallery extends AppCompatActivity {
    private ActivityPickImageFromGalleryBinding binding;
    public PickImageFromGallery() {
    }

    public static final int PICK_ME_REQUEST_CODE =10;

    public Intent getIntent(Context context){
        //This Method is Used to Get the Images from the Gallery with the Default Picker
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        return openGallery;

    }
    public void goToPickActivity(Context context){
        Intent pickImageIntent = new Intent(context,PickImageFromGallery.class);
        //You need to Add the Context parameter in Order to use the Activity Methods Outside onCreate of any activity context.Methods-Name.
        context.startActivity(pickImageIntent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickImageFromGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*startActivityForResult(getIntent(this),PICK_ME_REQUEST_CODE);*/
        fetchGalleryImagesIntoRecyclerView(); // Working on the Query of this Method to fetch the Images
        /*getVideosFromGallery();*/ // This Method is Used to get the Video Stored in the Local Phone Storage.

        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode == PickImageFromGallery.PICK_ME_REQUEST_CODE){
            Intent previewIntent = new Intent(PickImageFromGallery.this,PreviewActivity.class);
            previewIntent.putExtra("MEDIA_PATH",data.getData().toString());
            if(data.getData().toString().contains("image")){
                previewIntent.putExtra("MEDIA_TYPE","IMAGE");
            }else {
                previewIntent.putExtra("MEDIA_TYPE","VIDEO");
            }

            startActivity(previewIntent);
        }

    }
    private List<Image> fetchGalleryImagesIntoRecyclerView(){

        List<Image> imageList = new ArrayList<Image>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };
        String selection = MediaStore.Images.Media.AUTHOR +
                " >= ?";
        String[] selectionArgs = new String[] {""};
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                imageList.add(new Image(contentUri, name, size));
            }
        }


        Log.i("PICK_ME",imageList.size()+" IMAGE LIST SIZE");
       /* binding.recvImages.setAdapter(new GalleryAdapter(PickImageFromGallery.this,imageList));
        binding.recvImages.setLayoutManager(new GridLayoutManager(PickImageFromGallery.this,3));
*/
        return imageList;


    }
    private List<Video> getVideosFromGallery(){
        List<Video> videoList = new ArrayList<Video>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))};
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList.add(new Video(contentUri, name, duration, size));
            }
        }


        Log.i("PICK_ME",videoList.size()+" VIDEO LIST SIZE");
           binding.recvImages.setAdapter(new GalleryVideoAdapter(PickImageFromGallery.this,videoList));
        binding.recvImages.setLayoutManager(new GridLayoutManager(PickImageFromGallery.this,3));
        return videoList;

    }



}