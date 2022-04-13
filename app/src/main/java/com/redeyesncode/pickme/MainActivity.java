package com.redeyesncode.pickme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.redeyesncode.pickme.databinding.ActivityMainBinding;
import com.redeyesncode.pickmeredeyesncode.PickImageFromGallery;

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
                PickImageFromGallery pickImageFromGallery = new PickImageFromGallery();
                pickImageFromGallery.goToPickActivity(MainActivity.this);

            }
        });
    }
}