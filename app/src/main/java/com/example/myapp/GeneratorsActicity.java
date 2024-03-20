package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GeneratorsActicity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generators_acticity);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnSwichToFirstActivity = (Button) findViewById(R.id.backButton1);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneratorsActicity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnSwichToFirstActivity.setOnClickListener(onClickListener);



        Button btnSwichToDropsActivity = (Button) findViewById(R.id.buttonEnterDrops);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GeneratorsActicity.this, DropsActivity.class);
                startActivity(intent1);
            }
        };
        btnSwichToDropsActivity.setOnClickListener(onClickListener1);

        Button btnSwichToCurlsActivity = (Button) findViewById(R.id.buttonEnterCurls);
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(GeneratorsActicity.this, CurlsActivity.class);
                startActivity(intent2);
            }
        };
        btnSwichToCurlsActivity.setOnClickListener(onClickListener2);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnSwichToTilingActivity = (Button) findViewById(R.id.buttonEnterTiling);
        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(GeneratorsActicity.this, TilingActivity.class);
                startActivity(intent3);
            }
        };
        btnSwichToTilingActivity.setOnClickListener(onClickListener3);
    }
}