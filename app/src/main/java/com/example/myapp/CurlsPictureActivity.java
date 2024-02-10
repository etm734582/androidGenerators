package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.paintingtools.CurlsBitmapHolder;
import com.example.paintingtools.DropsBitmapHolder;
import com.example.supporttools.SaveImage;

public class CurlsPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curls_picture);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = (ImageView) findViewById(R.id.imageViewCurlsPic);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button showButton = (Button) findViewById(R.id.buttonCurlsPicShow);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(CurlsBitmapHolder.getBitmap());
            }
        };
        showButton.setOnClickListener(onClickListener);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.buttonCurlsPicBack);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurlsPictureActivity.this, CurlsActivity.class);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener1);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button saveButton = (Button) findViewById(R.id.buttonCurlsPicSave);
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CurlsPictureActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CurlsPictureActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7777777);
                }
                SaveImage.saveImage(CurlsPictureActivity.this, CurlsBitmapHolder.getBitmap());
            }
        };
        saveButton.setOnClickListener(onClickListener2);
    }
}