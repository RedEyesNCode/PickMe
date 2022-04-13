package com.redeyesncode.pickmeredeyesncode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.redeyesncode.pickmeredeyesncode.databinding.ActivityPickImageFromGalleryBinding;

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
        startActivityForResult(getIntent(this),PICK_ME_REQUEST_CODE);
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
}