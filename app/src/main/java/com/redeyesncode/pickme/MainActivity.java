package com.redeyesncode.pickme;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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

import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
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
    private ActivityResultLauncher<CropImageContractOptions> cropImage;

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

            }
        });




    }
    private void checkPermission(){
        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                  /*  PickImageFromGallery pickImageFromGallery = new PickImageFromGallery();
                    pickImageFromGallery.goToPickActivity(MainActivity.this); */
                    openCropperImage();

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
        cropImage = registerForActivityResult(
                new ActivityResultContract<CropImageContractOptions, CropImageView.CropResult>() {
                    @Override
                    public CropImageView.CropResult parseResult(int i, Intent intent) {
                        return null;
                    }

                    @Override
                    public Intent createIntent(Context context, CropImageContractOptions cropImageContractOptions) {
                        return null;
                    }
                }, new ActivityResultCallback<CropImageView.CropResult>() {
                    @Override
                    public void onActivityResult(CropImageView.CropResult result) {
                        Log.v("CROP_RESULT", result.toString());
                    }
                });

        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.cropMenuCropButtonTitle = "Done";
        cropImageOptions.guidelines = CropImageView.Guidelines.ON;
        /*PickImageContractOptions pickImageContractOptions = new PickImageContractOptions();
        pickImageContractOptions.setIncludeCamera(true);
        pickImageContractOptions.setIncludeGallery(true);*/



        CropImageContractOptions options = new CropImageContractOptions(null, cropImageOptions);
        cropImage.launch(options);


    }


}