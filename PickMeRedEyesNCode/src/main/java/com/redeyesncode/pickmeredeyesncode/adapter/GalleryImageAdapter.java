package com.redeyesncode.pickmeredeyesncode.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.redeyesncode.pickmeredeyesncode.R;
import com.redeyesncode.pickmeredeyesncode.databinding.MediaListBinding;
import com.redeyesncode.pickmeredeyesncode.model.Image;

import java.util.List;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.MyViewholder> {

    private Context context;
    private List<Image> filesPaths;
    private onClicked onClicked;


    public GalleryImageAdapter(Context context, List<Image> filesPaths,onClicked onClicked) {
        this.context = context;
        this.filesPaths = filesPaths;
        this.onClicked = onClicked;

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

        if(image.getImagePath().contains("REDEYESNCODE")){
            Glide.with(context).load(R.drawable.ic_add_image).into(holder.binding.ImageTumbnail);
            holder.binding.ImageTumbnail.setPadding(100,100,100,100);
        }else {
            Glide.with(context).load(image.getImagePath()).into(holder.binding.ImageTumbnail);
        }

        holder.binding.ImageTumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image.getImagePath().contains("REDEYESNCODE")){
                    onClicked.onImageClick(position,"REDEYESNCODE",image.getUri());
                }else {
                    onClicked.onImageClick(position,image.getImagePath(),image.getUri());
                }
            }
        });





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
    public interface onClicked{
        void onImageClick(int position, String name , Uri uri);
    }
}
