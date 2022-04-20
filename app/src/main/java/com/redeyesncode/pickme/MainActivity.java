package com.redeyesncode.pickme;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.redeyesncode.pickme.databinding.ActivityMainBinding;
import com.redeyesncode.pickmeredeyesncode.view.PickImageFromGallery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SimpleExoPlayer player;
    static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    static final String FILE_NAMING_PREFIX = "JPEG_";
    static final String FILE_NAMING_SUFFIX = "_";
    static final String FILE_FORMAT = ".jpg";
    static final String AUTHORITY_SUFFIX = ".cropper.fileprovider";
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(),
            new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult result) {


                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //ACTIVITY WHERE WE NEED TO CALL THE PICK IMAGE ACTIVITY.

        binding.btnPickMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermission();
            }
        });


        binding.btnCropperActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();

            }
        });




    }
    private void checkPermission(){
        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                    PickImageFromGallery pickImageFromGallery = new PickImageFromGallery();
                    pickImageFromGallery.goToPickActivity(MainActivity.this);
//                    openCropperImage();

                } else {
                    showDialogSettings();
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    private void showDialogSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permission");
        builder.setMessage("This app needs permission to Download Shipping Label. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PickImageFromGallery.PICK_ME_REQUEST_CODE_GALLERY){
            try {
                String mediaType = data.getStringExtra("MEDIA_TYPE");
                if (mediaType.contains("BITMAP")) {
                    Bitmap bitmapMain = (Bitmap) data.getParcelableExtra("BITMAP_");
                    binding.setMediaImage.setImageBitmap(bitmapMain);
                }else if(mediaType.contains("VIDEO")){
                    String uriFinal = data.getStringExtra("URI_FINAL");
                    Uri pickedUri = Uri.parse(uriFinal);
                    Log.i("PICK_ME : MAIN ACTIVITY",uriFinal);
                    binding.setMediaImage.setVisibility(View.GONE);
                    binding.previewVideo.setVisibility(View.VISIBLE);
                    binding.previewVideo.setVisibility(View.VISIBLE);
                    binding.previewVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



                    player = new SimpleExoPlayer.Builder(this).build();
                    MediaItem mediaItem = MediaItem.fromUri(pickedUri);
                    player.setMediaItem(mediaItem);
                    binding.previewVideo.setPlayer(player);

                    player.prepare();
                    player.play();
                    /*binding.setMediaImage.setImageURI(pickedUri);*/


                } else {

                    String uriFinal = data.getStringExtra("URI_FINAL");
                    Uri PICKEDuri = Uri.parse(uriFinal);
                    Log.i("PICK_ME : MAIN ACTIVITY :: LAST ELSE",uriFinal);
                    binding.setMediaImage.setImageURI(PICKEDuri);

                }


            }catch (Exception e){
                Log.i("PICK_ME",e.getMessage());
                Toast.makeText(MainActivity.this, "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void openCropperImage(){

        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.cropMenuCropButtonTitle = "Done";
        cropImageOptions.guidelines = CropImageView.Guidelines.ON;
     /*   PickImageContractOptions pickImageContractOptions = new PickImageContractOptions();
        pickImageContractOptions.setIncludeCamera(true);
        pickImageContractOptions.setIncludeGallery(true);*/



        CropImageContractOptions options = new CropImageContractOptions(null, cropImageOptions)
                .setBackgroundColor(getResources().getColor(R.color.black))
                .setCropMenuCropButtonTitle("DONE IMAGE")
                .setActivityTitle("done").setAllowFlipping(true);

        cropImage.launch(options);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(binding.previewVideo.getVisibility()==View.VISIBLE){
            player.stop();
        }
    }
}