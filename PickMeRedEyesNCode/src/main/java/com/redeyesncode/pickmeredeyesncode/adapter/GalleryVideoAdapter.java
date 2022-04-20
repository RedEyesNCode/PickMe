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
import com.redeyesncode.pickmeredeyesncode.model.Video;

import java.util.List;

public class GalleryVideoAdapter extends RecyclerView.Adapter<GalleryVideoAdapter.MyViewholder>{

    private Context context;
    private List<Video> filesPaths;
    private onClicked onClicked;

    public GalleryVideoAdapter(Context context, List<Video> filesPaths,onClicked onClicked) {
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
        Video video = filesPaths.get(position);

        if(video.getName().contains("REDEYESNCODE")){
            Glide.with(context).load(R.drawable.video).into(holder.binding.ImageTumbnail);
            holder.binding.ImageTumbnail.setPadding(100,100,100,100);
        }else {
            Glide.with(context).load(video.getUri()).into(holder.binding.ImageTumbnail);
        }

        holder.binding.ImageTumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(video.getName().contains("REDEYESNCODE")){
                    onClicked.onVideoClick(position,"REDEYESNCODE",video.getUri());

                }else {
                    onClicked.onVideoClick(position,video.getName(),video.getUri());
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
        void onVideoClick(int position, String name, Uri uri);
    }

}
