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

import com.example.paintingtools.TilingBitmapHolder;
import com.example.supporttools.SaveImage;

public class TilingPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiling_picture);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = findViewById(R.id.imageTilingViewShow);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonShow = findViewById(R.id.buttonTilingShow);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.buttonTilingBack);
        Button buttonSave = findViewById(R.id.buttonTilingSave);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(TilingBitmapHolder.getBitmap());
            }
        };
        buttonShow.setOnClickListener(onClickListener);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TilingPictureActivity.this, TilingActivity.class);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TilingPictureActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TilingPictureActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7777777);
                }
                SaveImage.saveImage(TilingPictureActivity.this, TilingBitmapHolder.getBitmap());
            }
        };
        buttonSave.setOnClickListener(onClickListener2);
    }
}