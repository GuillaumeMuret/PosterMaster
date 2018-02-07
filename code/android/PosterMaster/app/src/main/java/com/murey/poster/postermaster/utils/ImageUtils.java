package com.murey.poster.postermaster.utils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageUtils {

    public static ImageView setImageLayout(int margin, ImageView src){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(margin, margin, margin, margin);
        src.setLayoutParams(lp);
        return src;
    }

    public static void loadPhoto(String path, ImageView destination) {
        Picasso.with(destination.getContext())
                .load(new File(path))
                .fit()
                .centerCrop()
                .into(destination);
    }

    public static boolean isFileImage(String filePath){
        boolean isFileImage = false;
        String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
        try {
            File file = new File(filePath);
            if(file.exists()) {
                for (String extension : okFileExtensions) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        LogUtils.d(LogUtils.DEBUG_TAG, "File is image");
                        isFileImage = true;
                    }
                }
            }
        }catch(Exception e){
            LogUtils.e(LogUtils.DEBUG_TAG,"ERROR Exception Image Utils => ",e);
            isFileImage = false;
        }
        return isFileImage;
    }
}
