package com.redeyesncode.pickmeredeyesncode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.redeyesncode.pickmeredeyesncode.databinding.MediaListBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewholder> {

    private Context context;
    private List<String> filesPaths;

    public GalleryAdapter(Context context, List<String> filesPaths) {
        this.context = context;
        this.filesPaths = filesPaths;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MediaListBinding mediaListBinding = MediaListBinding.inflate(LayoutInflater.from(context),parent,false);

        return new MyViewholder(mediaListBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        Glide.with(context).load(filesPaths.get(position)).centerCrop().into(holder.binding.ImageTumbnail);


    }

    @Override
    public int getItemCount() {
        return filesPaths.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder{

        MediaListBinding binding;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            binding = MediaListBinding.bind(itemView);

        }
    }
}
