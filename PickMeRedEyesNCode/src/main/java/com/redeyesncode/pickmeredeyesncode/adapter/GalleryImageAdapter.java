package com.redeyesncode.pickmeredeyesncode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.redeyesncode.pickmeredeyesncode.databinding.MediaListBinding;
import com.redeyesncode.pickmeredeyesncode.model.Image;

import java.util.List;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.MyViewholder> {

    private Context context;
    private List<Image> filesPaths;

    public GalleryImageAdapter(Context context, List<Image> filesPaths) {
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
        Image image = filesPaths.get(position);

        if(!image.getImagePath().isEmpty()){
            Glide.with(context).load(image.getImagePath()).into(holder.binding.ImageTumbnail);
        }else {
            holder.binding.ImageTumbnail.setImageResource(image.getResourceId());
        }





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
