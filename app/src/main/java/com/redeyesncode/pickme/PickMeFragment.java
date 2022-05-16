package com.redeyesncode.pickme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.redeyesncode.pickme.databinding.FragmentPickMeBinding;
import com.redeyesncode.pickmeredeyesncode.view.RedEyesNCodeListener;
import com.redeyesncode.pickmeredeyesncode.view.RedEyesNCode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PickMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickMeFragment extends Fragment implements RedEyesNCodeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentPickMeBinding binding;
    private Context context;
    SimpleExoPlayer player;

    @Override
    public void onImageReceive(Uri uri) {
        binding.setMediaImage.setImageURI(uri);

        binding.setMediaImage.setVisibility(View.VISIBLE);
        binding.previewVideo.setVisibility(View.GONE);
    }

    @Override
    public void onVideoReceive(Uri uri) {
        player = new SimpleExoPlayer.Builder(context).build();
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        binding.previewVideo.setPlayer(player);
        binding.setMediaImage.setVisibility(View.GONE);
        binding.previewVideo.setVisibility(View.VISIBLE);
        player.prepare();
        player.play();
    }

    @Override
    public void onBitmapReceive(Bitmap bitmap) {
        binding.setMediaImage.setImageBitmap(bitmap);
        binding.setMediaImage.setVisibility(View.VISIBLE);
        binding.previewVideo.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PickMeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PickMeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PickMeFragment newInstance(String param1, String param2) {
        PickMeFragment fragment = new PickMeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPickMeBinding.inflate(inflater,container,false);
        binding.btnPickMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });



        return binding.getRoot();
    }

    private void checkPermission(){
        Dexter.withContext(context).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                   redEyesNCode();
                } else {
                    showDialogSettings();
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    private void showDialogSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permission");
        builder.setMessage("This app needs permission to Download Shipping Label. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RedEyesNCode.PICK_ME_REQUEST_CODE_GALLERY){
            try {
                String mediaType = data.getStringExtra("MEDIA_TYPE");
                if (mediaType.contains("BITMAP")) {
                    Bitmap bitmapMain = (Bitmap) data.getParcelableExtra("BITMAP_");
                    binding.setMediaImage.setImageBitmap(bitmapMain);
                }else if(mediaType.contains("VIDEO")){
                    String uriFinal = data.getStringExtra("URI_FINAL");
                    Uri pickedUri = Uri.parse(uriFinal);
                    Log.i("PICK_ME : MAIN ACTIVITY",uriFinal);
                    binding.setMediaImage.setVisibility(View.GONE);
                    binding.previewVideo.setVisibility(View.VISIBLE);
                    binding.previewVideo.setVisibility(View.VISIBLE);
                    binding.previewVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    player = new SimpleExoPlayer.Builder(context).build();
                    MediaItem mediaItem = MediaItem.fromUri(pickedUri);
                    player.setMediaItem(mediaItem);
                    binding.previewVideo.setPlayer(player);

                    player.prepare();
                    player.play();
                    binding.setMediaImage.setImageURI(pickedUri);


                } else {

                    String uriFinal = data.getStringExtra("URI_FINAL");
                    Uri PICKEDuri = Uri.parse(uriFinal);
                    Log.i("PICK_ME :",uriFinal);
                    binding.setMediaImage.setImageURI(PICKEDuri);

                }


            }catch (Exception e){
                Log.i("PICK_ME",e.getMessage());
                Toast.makeText(context, "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void redEyesNCode(){
        RedEyesNCode redEyesNCode = new RedEyesNCode();
        redEyesNCode.initFragment(getActivity(),this);
    }
}