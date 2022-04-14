package com.redeyesncode.pickme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

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
    }
    private void checkPermission(){
        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                    PickImageFromGallery pickImageFromGallery = new PickImageFromGallery();
                    pickImageFromGallery.goToPickActivity(MainActivity.this);                }else {
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
        if(requestCode==PickImageFromGallery.PICK_ME_REQUEST_CODE){
            try {
                String uriFinal = data.getStringExtra("URI_FINAL");
                Uri PICKEDuri = Uri.parse(uriFinal);
                binding.setMediaImage.setImageURI(PICKEDuri);

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }

        }

    }
}