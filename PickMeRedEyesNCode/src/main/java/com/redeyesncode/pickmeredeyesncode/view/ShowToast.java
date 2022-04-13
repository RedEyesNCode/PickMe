package com.redeyesncode.pickmeredeyesncode.view;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

    public static void s(Context c, String message){

        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();

    }

}
