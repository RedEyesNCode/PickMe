package com.redeyesncode.pickmeredeyesncode.view;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.redeyesncode.pickmeredeyesncode.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity {

    private ActivityPreviewBinding binding;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        
    }
}