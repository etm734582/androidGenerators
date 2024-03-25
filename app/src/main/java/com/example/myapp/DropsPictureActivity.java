package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.paintingtools.DropsBitmapHolder;
import com.example.supporttools.SaveImage;

public class DropsPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops_picture);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewPicDrops);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button showButton = (Button) findViewById(R.id.buttonDropsShow);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(DropsBitmapHolder.getBitmap());
            }
        };
        showButton.setOnClickListener(onClickListener);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.buttonDropsPicBack);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DropsPictureActivity.this, DropsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener1);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button saveButton = (Button) findViewById(R.id.buttonDropsSave);
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DropsPictureActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DropsPictureActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7777777);
                }
                SaveImage.saveImage(DropsPictureActivity.this, DropsBitmapHolder.getBitmap());
            }
        };
        saveButton.setOnClickListener(onClickListener2);
    }
}