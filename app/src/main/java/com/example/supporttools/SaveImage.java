package com.example.supporttools;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.myapp.DropsPictureActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveImage {
    public static void saveImage(Context context, Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpeg";

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri uri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentResolver resolver = context.getContentResolver();
        Uri imageUri = resolver.insert(uri, values);
        try {
            if (imageUri != null) {
                OutputStream stream = resolver.openOutputStream(imageUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                stream.flush();
                stream.close();

                Toast.makeText(context, "Image saveed to gallary", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error aving image",  Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
