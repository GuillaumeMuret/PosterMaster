package com.murey.poster.postermaster.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.murey.poster.postermaster.model.database.tables.Posters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class FileUtils {

    private static final String PNG_EXTENSION = ".png";
    private static final String NAME_DIRECTORY = "PosterMasterDir";

    public static String savePosterFile(Context context, String filename, Bitmap bitmap){
        saveInEnvironmentData(context, filename, bitmap);
        return saveInSharedDirectory(context, bitmap);
    }

    public static String saveInSharedDirectory(Context context, Bitmap bitmap){
        ContentValues values = new ContentValues();
        values.put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream os  = context.getContentResolver().openOutputStream(uri);
            LogUtils.d(LogUtils.DEBUG_TAG, "URI => " + uri.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        bitmap = null;
        System.gc();
        return uri.toString();
    }

    private static void saveBitmapIntoFile(Context context, String filename, Bitmap bitmap){
        //create a file to write bitmap data
        File f = new File(context.getFileStreamPath(filename).getPath());
        try {
            f.createNewFile();

            LogUtils.d(LogUtils.DEBUG_TAG,"Save file in path => " + f.getAbsolutePath());

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveInEnvironmentData(Context context, String filename, Bitmap bitmap){
        saveBitmapIntoFile(context, filename, bitmap);
    }

    public static void setImageBitmapFromFile(ImageView img, String filename){
        Bitmap bitmap = getBitmapFromFilename(img.getContext(), filename);
        img.setImageBitmap(bitmap);
    }

    public static Bitmap getBitmapFromFilename(Context context, String filename){
        String filePath = context.getFileStreamPath(filename).getPath();
        return BitmapFactory.decodeFile(filePath);
    }
}
