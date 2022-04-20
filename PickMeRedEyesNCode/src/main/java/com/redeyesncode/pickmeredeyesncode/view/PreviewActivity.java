package com.redeyesncode.pickmeredeyesncode.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.redeyesncode.pickmeredeyesncode.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity {

    private ActivityPreviewBinding binding;

    Uri previewUri = MediaStore.getMediaScannerUri();
    SimpleExoPlayer player;
    @Override
    protected void onStop() {
        super.onStop();
        if(binding.previewVideo.getVisibility()==View.VISIBLE){
            player.stop();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

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
            binding.previewVideo.setVisibility(View.GONE);
            Glide.with(PreviewActivity.this).load(previewUri).into(binding.ivMediaImage);

        }else if(mediaType.contains("VIDEO")){
            String mediaUri = getIntent().getStringExtra("VIDEO_PATH");
            Uri videoPreviewUri = Uri.parse(mediaUri);
            Log.i("PICK_ME : VIDEO",videoPreviewUri.toString());
            binding.ivMediaImage.setVisibility(View.GONE);
            binding.previewVideo.setVisibility(View.VISIBLE);
            binding.previewVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



            player = new SimpleExoPlayer.Builder(this).build();
            MediaItem mediaItem = MediaItem.fromUri(videoPreviewUri);
            player.setMediaItem(mediaItem);
            binding.previewVideo.setPlayer(player);

            player.prepare();
            player.play();






            // LOADING THE EXO PLAYER TO PLAY THE VIDEO HERE.

        }else if(mediaType.contains("BITMAP")){
            Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("IMAGE_PATH");
            binding.ivMediaImage.setVisibility(View.VISIBLE);
            binding.ivMediaImage.setImageBitmap(bitmap);

        }
/*https://developer.android.com/training/camera/photobasics*/
/*@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        imageView.setImageBitmap(imageBitmap);
    }
}
*/
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
                if(mediaType.contains("BITMAP")){
                    Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("IMAGE_PATH");
                    backWithUriDataIntent.putExtra("MEDIA_TYPE","BITMAP");
                    backWithUriDataIntent.putExtra("BITMAP_", bitmap);
                    setResult(81,backWithUriDataIntent);
                    finish();
                }else if(mediaType.contains("VIDEO")){
                    backWithUriDataIntent.putExtra("MEDIA_TYPE","VIDEO");
                    backWithUriDataIntent.putExtra("URI_FINAL",previewUri.toString());

                    setResult(77,backWithUriDataIntent);
                    finish();



                }else {
                    backWithUriDataIntent.putExtra("MEDIA_TYPE","IMAGE");
                    backWithUriDataIntent.putExtra("URI_FINAL",previewUri.toString());
                    setResult(81,backWithUriDataIntent);
                    finish();
                }



            }
        });

        
    }
}