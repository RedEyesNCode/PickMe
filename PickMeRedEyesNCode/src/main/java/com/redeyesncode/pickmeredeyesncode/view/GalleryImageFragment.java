package com.redeyesncode.pickmeredeyesncode.view;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.ListenableFuture;
import com.redeyesncode.pickmeredeyesncode.R;
import com.redeyesncode.pickmeredeyesncode.adapter.GalleryImageAdapter;
import com.redeyesncode.pickmeredeyesncode.databinding.FragmentGalleryImageBinding;
import com.redeyesncode.pickmeredeyesncode.model.Image;
import com.redeyesncode.pickmeredeyesncode.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryImageFragment extends Fragment implements GalleryImageAdapter.onClicked{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private FragmentGalleryImageBinding binding;
    private final int CAMERA_PIC_REQUEST = 35;
    private String mTempPhotoPath;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;
    private  List<Image> imageList = new ArrayList<Image>();
    private GalleryImageAdapter galleryImageAdapter;


    @Override
    public void onImageClick(int position, String name, Uri uri) {
        if(name.contains("REDEYESNCODE")){
            //CODE TO STORE THE IMAGE USING THE IMAGE URI

            // THESE ARE THE OLD METHODS TO TAKE THE CAMERA PHOTOS.
            cameraIntent();



        }else {

            Intent previewIntent = new Intent(context,PreviewActivity.class);
            previewIntent.putExtra("MEDIA_TYPE","IMAGE");
            previewIntent.putExtra("IMAGE_PATH",uri.toString());
            startActivityForResult(previewIntent,PickImageFromGallery.PICK_ME_IMAGE_CODE);

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryImageFragment newInstance(String param1, String param2) {
        GalleryImageFragment fragment = new GalleryImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGalleryImageBinding.inflate(inflater,container,false);
        fetchGalleryImagesIntoRecyclerView();

        return  binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK){
            try {

                Log.i("PICK_ME",data.getExtras().get("data").toString());
                Intent previewIntent = new Intent(context,PreviewActivity.class);
                previewIntent.putExtra("MEDIA_TYPE","BITMAP");

                //GETTING THE CLICKED IMAGE BITMAP//

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                previewIntent.putExtra("IMAGE_PATH",bitmap);
                startActivityForResult(previewIntent,PickImageFromGallery.PICK_ME_IMAGE_CODE);


            }catch (Exception e){
                Log.i("PICK_ME",e.getMessage());
            }


        }else if(requestCode==PickImageFromGallery.PICK_ME_IMAGE_CODE){
            try {
                String mediaType = data.getStringExtra("MEDIA_TYPE");
                if(mediaType.contains("BITMAP")){
                    Bitmap bitmapfinal = (Bitmap) data.getParcelableExtra("BITMAP_");
                    Intent backWithUriDataIntent = new Intent();
                    backWithUriDataIntent.putExtra("MEDIA_TYPE","BITMAP");
                    backWithUriDataIntent.putExtra("BITMAP_", bitmapfinal);
                    saveCameraImage(data);
                    getActivity().setResult(PickImageFromGallery.PICK_ME_REQUEST_CODE_GALLERY,backWithUriDataIntent);
                    getActivity().onBackPressed();
                }else {
                    String uriFinal = data.getStringExtra("URI_FINAL");
                    Intent backWithUriDataIntent = new Intent();
                    backWithUriDataIntent.putExtra("MEDIA_TYPE","IMAGE");
                    backWithUriDataIntent.putExtra("URI_FINAL",uriFinal);

                    //USING GET ACTIVITY IN FRAGMENT
                    getActivity().setResult(PickImageFromGallery.PICK_ME_REQUEST_CODE_GALLERY,backWithUriDataIntent);
                    getActivity().onBackPressed();
                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }

    private void saveCameraImage(Intent data){
        //THIS METHOD IS USED TO SAVE THE IMAGE CLICKED IN THE ANDROID DEVICE.


    }
    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchGalleryImagesIntoRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        fetchGalleryImagesIntoRecyclerView();
    }

    private List<Image> fetchGalleryImagesIntoRecyclerView(){



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
        try (Cursor cursor = context.getContentResolver().query(
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
            imageList.add(new Image(uri,"REDEYESNCODE","",0,R.drawable.ic_add_image));
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
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                /*imageList.add(new Image(contentUri,"","",0,R.drawable.ic_add_image));*/


                if(mediaTypeFinal==1){
                    imageList.add(new Image(contentUri, originalPath,name, size,0));
                }


            }
        }


        Log.i("PICK_ME",imageList.size()+" IMAGE LIST SIZE");

        galleryImageAdapter = new GalleryImageAdapter(context,imageList,this);
        binding.recvImages.setAdapter(galleryImageAdapter);
        binding.recvImages.setLayoutManager(new GridLayoutManager(context,2));
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

        try (Cursor cursor = context.getContentResolver().query(
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
        /*binding.recvImages.setAdapter(new GalleryVideoAdapter(context,videoList,this));
        binding.recvImages.setLayoutManager(new GridLayoutManager(context,2));*/
        return videoList;

    }


    //BELOW IS THE IMPLEMENTATION OF THE CameraX Feature Introduced in Android


}
