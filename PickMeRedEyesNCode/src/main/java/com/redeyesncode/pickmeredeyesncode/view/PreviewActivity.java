package com.redeyesncode.pickmeredeyesncode.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.redeyesncode.pickmeredeyesncode.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity {

    private ActivityPreviewBinding binding;

    Uri previewUri = MediaStore.getMediaScannerUri();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String mediaType = getIntent().getStringExtra("MEDIA_TYPE");
        if (mediaType.contains("IMAGE")){
            String mediaUri = getIntent().getStringExtra("IMAGE_PATH");
            previewUri = Uri.parse(mediaUri);
            binding.ivMediaImage.setVisibility(View.VISIBLE);
            Glide.with(PreviewActivity.this).load(previewUri).into(binding.ivMediaImage);

        }else if(mediaType.contains("VIDEO")){
            String mediaUri = getIntent().getStringExtra("VIDEO_PATH");
            previewUri = Uri.parse(mediaUri);
            // LOADING THE EXO PLAYER TO PLAY THE VIDEO HERE.

        }


        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backWithUriDataIntent = new Intent();
                backWithUriDataIntent.putExtra("URI_FINAL",previewUri.toString());
                setResult(81,backWithUriDataIntent);
                finish();
            }
        });

        
    }
}