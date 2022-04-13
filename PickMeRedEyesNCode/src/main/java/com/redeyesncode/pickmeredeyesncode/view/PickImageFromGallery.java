package com.redeyesncode.pickmeredeyesncode.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.tabs.TabLayout;
import com.redeyesncode.pickmeredeyesncode.adapter.GalleryImageAdapter;
import com.redeyesncode.pickmeredeyesncode.adapter.GalleryVideoAdapter;
import com.redeyesncode.pickmeredeyesncode.adapter.TabsAdapter;
import com.redeyesncode.pickmeredeyesncode.model.Image;
import com.redeyesncode.pickmeredeyesncode.model.Video;
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

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Images"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(),binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(tabsAdapter);
        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0 || tab.getPosition()==1){
                    tabsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




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
            Intent previewIntent = new Intent(PickImageFromGallery.this, PreviewActivity.class);
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
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };

        String[] strArr = {"_data", "mime_type","media_type", "_display_name","_data", "datetaken", "_size", "width", "height", "_id"
                , "title", "bucket_id", "bucket_display_name", "date_added", "date_modified"};
        //these are the things we need to query from the

        String selection = MediaStore.Images.Media.AUTHOR +
                " >= ?";
        String[] selectionArgs = new String[] {""};


        String sortOrder = MediaStore.Files.FileColumns.DATE_TAKEN+" DESC"; //ASC // DESC //SORTING DONE ADDING THE IMAGES ACCORDING TO THE LATEST IMAGE IS SHOWN FIRST.


        Uri uri = MediaStore.Files.getContentUri("external"); // THIS THE URI USED TO QUERY THE PARAMETERS USED. FOR GETTING THE IMAGES FROM THE GALLERY AND THE IMAGES.
        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                uri,
                strArr,
                null, //YOU NEED TO ADD THE NULL PARAMETERS FOR THE CHANGE IN THE URI.
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            //ADDED THIS TO CHECK THE TYPE OF THE MEDIA AND ADD ONLY THE IMAGES TYPE IN THE MEDIA.

            int mediaType = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);
                int mediaTypeFinal = cursor.getInt(mediaType);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                System.out.println("URI: "+contentUri.toString());


                String originalPath = cursor.getString(cursor.getColumnIndex("_data"));
                System.out.println("ORIGINAL_PATH: "+originalPath);
                // Stores column
                // values and the contentUri in a local object
                // that represents the media file.

                if(mediaTypeFinal==1){
                    imageList.add(new Image(contentUri, originalPath,name, size,0));
                }


            }
        }


        Log.i("PICK_ME",imageList.size()+" IMAGE LIST SIZE");


        binding.recvImages.setAdapter(new GalleryImageAdapter(PickImageFromGallery.this,imageList));
        binding.recvImages.setLayoutManager(new GridLayoutManager(PickImageFromGallery.this,2));
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
        binding.recvImages.setLayoutManager(new GridLayoutManager(PickImageFromGallery.this,2));
        return videoList;

    }



}