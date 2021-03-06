package com.redeyesncode.pickmeredeyesncode.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.redeyesncode.pickmeredeyesncode.view.GalleryImageFragment;
import com.redeyesncode.pickmeredeyesncode.view.GalleryVideoFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public TabsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new GalleryImageFragment();
            case 1: return new GalleryVideoFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
