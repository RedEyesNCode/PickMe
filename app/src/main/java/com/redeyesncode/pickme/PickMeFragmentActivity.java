package com.redeyesncode.pickme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.redeyesncode.pickme.databinding.ActivityPickMeFragmentBinding;
import com.redeyesncode.pickme.utils.FragmentUtils;
import com.redeyesncode.pickmeredeyesncode.view.RedEyesNCode;

public class PickMeFragmentActivity extends AppCompatActivity {
    private ActivityPickMeFragmentBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickMeFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initClicks();
        FragmentUtils.replaceFragment(getSupportFragmentManager(),R.id.container,new PickMeFragment(),PickMeFragment.class.getSimpleName());
    }

    private void initClicks(){
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }
}