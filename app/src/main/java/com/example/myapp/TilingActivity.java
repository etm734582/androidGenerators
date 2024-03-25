package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.paintingtools.TilingBitmapHolder;
import com.example.paintingtools.TilingGeneratorCallable;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TilingActivity extends AppCompatActivity {
    final int defaultOutlineR = 1;
    final int defaultOutlineG = 1;
    final int defaultOutlineB = 1;
    final int defaultImSizeX = 1000;
    final int defaultImSizeY = 2000;
    final int defaultOutlineWidth = 15;
    final int defaultPltsX = 9;
    final int defaultPltsY = 10;
    final int defaultBrightness = 0;



    private SeekBar seekBarTilingImSizeX, seekBarTilingImSizeY, seekBarTilingOutlineWidth, seekBarTilingQtyAxisXPlts, seekBarTilingQtyAxisYPlts, seekBarTilingBright;
    private SeekBar seekBaTilingOutlineColorR, seekBaTilingOutlineColorG, seekBaTilingOutlineColorB;

    private TextView textViewTilingImSizeX, textViewTilingImSizeY, textViewTilingOutlineWidth, textViewTilingQtyAxisXPlts, textViewTilingQtyAxisYPlts, textViewTilingBrightness;
    private View viewTilingOutlineColor;

    SharedPreferences sharedPreferences;

    private TextView textViewTilingImageSizeXLabel, textViewTilingImageSizeYLabel, textViewTilingOutlineColorLabel;
    private TextView textViewTilingOutlineWidthLabel, textViewTilingXPlatesQntyLabel, textViewTilingYPlatesQntyLabelng;
    private TextView textViewTilingBrightnessLabel, textViewTilingHeader;

    private int outlineColorR, outlineColorG, outlineColorB;

    private int tilingImWidth, tilingImHeight, tilingOutlineWidth, tilingQtyOfTilesX, tilingQtyOfTilesY;
    private int outlineColor, tilingBrightness;

    @Override
    protected void onPause() {
        super.onPause();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        putOnPauseOneBarValues(editor);
        putOnPauseColors(editor);
    }
    @Override
    protected void onResume() {
        super.onResume();

        getColorsFromSP(sharedPreferences);
        getSettingsFromSP(sharedPreferences);
        resumeColors();
        resumeSettings();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiling);

        sharedPreferences = getSharedPreferences("TilingSettings", MODE_PRIVATE);

        getSeekBarOneBar();
        getTextViews();
        getForOutlineColor();
        getParametersNames();
        setDefaultColors();
        setListenerForOneBarParams(getOneBarSeekBarListener());
        setColorsSeekBarChangeListener();
        setOnClickListenerParams();

        viewTilingOutlineColor = findViewById(R.id.viewTilingOutlineColor);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.buttonBack);
        backButton.setOnClickListener(getBackBtnOCListener());
        Button runButton = findViewById(R.id.buttonTilingRun);




        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean tilingIsGenRunning = sharedPreferences.getBoolean("tilingIsGenRunning", false);
                if (tilingIsGenRunning) {
                    return;
                }
                sharedPreferences.edit().putBoolean("tilingIsGenRunning", true);

                runButton.setEnabled(false);
                backButton.setEnabled(false);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                setRunValues();

                //Toast.makeText(TilingActivity.this, "Error aving image",  Toast.LENGTH_SHORT).show();

                Callable<Bitmap> tilingGeneratorCallable = new TilingGeneratorCallable (
                        tilingQtyOfTilesX,
                        tilingQtyOfTilesY,
                        outlineColor,
                        tilingOutlineWidth,
                        tilingImWidth,
                        tilingImHeight,
                        tilingBrightness);
                Future<Bitmap> futureResult = executorService.submit(tilingGeneratorCallable);

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap image = futureResult.get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TilingBitmapHolder.setBitmap(image);
                                    runButton.setEnabled(true);
                                    backButton.setEnabled(true);
                                    Intent intent = new Intent(TilingActivity.this, TilingPictureActivity.class);
                                    sharedPreferences.edit().putBoolean("tilingIsGenRunning", true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                        } catch (ExecutionException e) {  // ???
                            throw new RuntimeException(e);  // ???
                        } catch (InterruptedException e) {  // ???
                            throw new RuntimeException(e);  // ???
                        }
                    }
                });

            }
        };
        runButton.setOnClickListener(listener);
    }


    private View.OnClickListener getOnClickListener (String title, String message) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TilingActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .show();
            }
        };
        return onClickListener;
    }
    private void setOnClickListenerParams () {
        textViewTilingImageSizeXLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.image_width), getResources().getString(R.string.explanation_size_x)));
        textViewTilingImageSizeYLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.image_height), getResources().getString(R.string.explanation_size_y)));
        textViewTilingOutlineColorLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.outline_color_tiling), getResources().getString(R.string.explanation_outline_color_tiling)));
        textViewTilingOutlineWidthLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.outline_width_tiling), getResources().getString(R.string.explanation_outline_width_tiling)));
        textViewTilingXPlatesQntyLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.quantity_of_plates_in_x_axis), getResources().getString(R.string.explanation_quantity_of_plates_in_x_axis)));
        textViewTilingYPlatesQntyLabelng.setOnClickListener(getOnClickListener(getResources().getString(R.string.quantity_of_plates_in_y_axis), getResources().getString(R.string.explanation_quantity_of_plates_in_y_axis)));
        textViewTilingBrightnessLabel.setOnClickListener(getOnClickListener(getResources().getString(R.string.brightness_label), getResources().getString(R.string.explanation_brightness_label)));
        textViewTilingHeader.setOnClickListener(getOnClickListener(getResources().getString(R.string.tiling_header), getResources().getString(R.string.explanation_tiling_header)));
    }
    private void setRunValues () {
        tilingImWidth = seekBarTilingImSizeX.getProgress();
        tilingImHeight = seekBarTilingImSizeY.getProgress();
        tilingOutlineWidth = seekBarTilingOutlineWidth.getProgress();
        tilingQtyOfTilesX = seekBarTilingQtyAxisXPlts.getProgress();
        tilingQtyOfTilesY = seekBarTilingQtyAxisYPlts.getProgress();
        outlineColor = Color.rgb(outlineColorR, outlineColorG, outlineColorB);
        tilingBrightness = seekBarTilingBright.getProgress();
    }
    private void updateOutlineColorIndecator () {
        int color = Color.rgb(outlineColorR, outlineColorG, outlineColorB);
        viewTilingOutlineColor.setBackgroundColor(color);
    }
    private void setColorsSeekBarChangeListener () {
        SeekBar.OnSeekBarChangeListener listener = getListenerForOutlineColor();
        seekBaTilingOutlineColorR.setOnSeekBarChangeListener(listener);
        seekBaTilingOutlineColorG.setOnSeekBarChangeListener(listener);
        seekBaTilingOutlineColorB.setOnSeekBarChangeListener(listener);
    }
    private SeekBar.OnSeekBarChangeListener getListenerForOutlineColor () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBaTilingOutlineColorR.getId()) {
                    outlineColorR = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBaTilingOutlineColorG.getId()) {
                    outlineColorG = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBaTilingOutlineColorB.getId()) {
                    outlineColorB = seekBar.getProgress();
                }
                updateOutlineColorIndecator();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        //
        // Как лучше сделать: проверять каждый seekbar цвета, или как сделано?
        //
        return listener;
    }
    private SeekBar.OnSeekBarChangeListener getOneBarSeekBarListener () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBarTilingImSizeX.getId()) {
                    textViewTilingImSizeX.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarTilingImSizeY.getId()) {
                    textViewTilingImSizeY.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarTilingOutlineWidth.getId()) {
                    textViewTilingOutlineWidth.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarTilingQtyAxisXPlts.getId()) {
                    textViewTilingQtyAxisXPlts.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarTilingQtyAxisYPlts.getId()) {
                    textViewTilingQtyAxisYPlts.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarTilingBright.getId()) {
                    textViewTilingBrightness.setText(String.valueOf((float) progress/100f));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        return listener;
    }
    private void setListenerForOneBarParams(SeekBar.OnSeekBarChangeListener listener) {
        seekBarTilingImSizeX.setOnSeekBarChangeListener(listener);
        seekBarTilingImSizeY.setOnSeekBarChangeListener(listener);
        seekBarTilingOutlineWidth.setOnSeekBarChangeListener(listener);
        seekBarTilingQtyAxisXPlts.setOnSeekBarChangeListener(listener);
        seekBarTilingQtyAxisYPlts.setOnSeekBarChangeListener(listener);
        seekBarTilingBright.setOnSeekBarChangeListener(listener);
    }
    private void getSeekBarOneBar () {
        seekBarTilingImSizeX = findViewById(R.id.seekBarTilingImSizeX);
        seekBarTilingImSizeY = findViewById(R.id.seekBarTilingImSizeY);
        seekBarTilingOutlineWidth = findViewById(R.id.seekBarTilingOutlineWidth);
        seekBarTilingQtyAxisXPlts = findViewById(R.id.seekBarTilingQtyAxisXPlts);
        seekBarTilingQtyAxisYPlts = findViewById(R.id.seekBarTilingQtyAxisYPlts);
        seekBarTilingBright = findViewById(R.id.seekBarTilingBright);
    }
    private void getTextViews () {
        textViewTilingImSizeX = findViewById(R.id.textViewTilingImSizeX);
        textViewTilingImSizeY = findViewById(R.id.textViewTilingImSizeY);
        textViewTilingOutlineWidth = findViewById(R.id.textViewTilingOutlineWidth);
        textViewTilingQtyAxisXPlts = findViewById(R.id.textViewTilingQntyOfX);
        textViewTilingQtyAxisYPlts = findViewById(R.id.textViewTilingQntyOfY);
        textViewTilingBrightness = findViewById(R.id.textViewTilingBrightness);
    }
    private void getForOutlineColor () {
        seekBaTilingOutlineColorR = findViewById(R.id.seekBaTilingOutlineColorR);
        seekBaTilingOutlineColorG = findViewById(R.id.seekBaTilingOutlineColorG);
        seekBaTilingOutlineColorB = findViewById(R.id.seekBaTilingOutlineColorB);
        viewTilingOutlineColor = findViewById(R.id.viewTilingOutlineColor);
    }
    private void setDefaultColors () {
        outlineColorR = 1;
        outlineColorG = 1;
        outlineColorB = 1;
    }
    private void getParametersNames () {
        textViewTilingImageSizeXLabel = findViewById(R.id.textViewTilingImageSizeXLabel);
        textViewTilingImageSizeYLabel = findViewById(R.id.textViewTilingImageSizeYLabel);
        textViewTilingOutlineColorLabel = findViewById(R.id.textViewTilingOutlineColorLabel);
        textViewTilingOutlineWidthLabel = findViewById(R.id.textViewTilingOutlineWidthLabel);
        textViewTilingXPlatesQntyLabel = findViewById(R.id.textViewTilingXPlatesQntyLabel);
        textViewTilingYPlatesQntyLabelng = findViewById(R.id.textViewTilingYPlatesQntyLabelng);
        textViewTilingBrightnessLabel = findViewById(R.id.textViewTilingBrightnessLabel);
        textViewTilingHeader = findViewById(R.id.textViewTilingHeader);
    }
    private void putOnPauseOneBarValues (SharedPreferences.Editor editor) {
        editor.putInt("tilingImWidth", seekBarTilingImSizeX.getProgress());
        editor.putInt("tilingImHeight", seekBarTilingImSizeY.getProgress());
        editor.putInt("tilingOutlineWidth", seekBarTilingOutlineWidth.getProgress());
        editor.putInt("tilingQtyOfTilesX", seekBarTilingQtyAxisXPlts.getProgress());
        editor.putInt("tilingQtyOfTilesY", seekBarTilingQtyAxisYPlts.getProgress());
        editor.putInt("tilingBrightness", seekBarTilingBright.getProgress());
        editor.apply();
    }
    private void putOnPauseColors (SharedPreferences.Editor editor) {
        editor.putInt("tilingOutlineColorR", outlineColorR);
        editor.putInt("tilingOutlineColorG", outlineColorG);
        editor.putInt("tilingOutlineColorB", outlineColorB);
        editor.apply();
    }
    private void getColorsFromSP (SharedPreferences sharedPreferences) {
        outlineColorR = sharedPreferences.getInt("tilingOutlineColorR", defaultOutlineR);
        outlineColorG = sharedPreferences.getInt("tilingOutlineColorG", defaultOutlineG);
        outlineColorB = sharedPreferences.getInt("tilingOutlineColorB", defaultOutlineB);
    }
    private void getSettingsFromSP (SharedPreferences sharedPreferences) {
        tilingImWidth = sharedPreferences.getInt("tilingImWidth", defaultImSizeX);
        tilingImHeight = sharedPreferences.getInt("tilingImHeight", defaultImSizeY);
        tilingOutlineWidth = sharedPreferences.getInt("tilingOutlineWidth", defaultOutlineWidth);
        tilingQtyOfTilesX = sharedPreferences.getInt("tilingQtyOfTilesX", defaultPltsX);
        tilingQtyOfTilesY = sharedPreferences.getInt("tilingQtyOfTilesY", defaultPltsY);
        tilingBrightness = sharedPreferences.getInt("tilingBrightness", defaultBrightness);
    }
    private void resumeColors () {
            seekBaTilingOutlineColorR.setProgress(outlineColorR);
            seekBaTilingOutlineColorG.setProgress(outlineColorG);
            seekBaTilingOutlineColorB.setProgress(outlineColorB);

            updateOutlineColorIndecator();
        }
    private void resumeSettings () {
        seekBarTilingImSizeX.setProgress(tilingImWidth);
        textViewTilingImSizeX.setText(String.valueOf(tilingImWidth));
        seekBarTilingImSizeY.setProgress(tilingImHeight);
        textViewTilingImSizeY.setText(String.valueOf(tilingImHeight));
        seekBarTilingOutlineWidth.setProgress(tilingOutlineWidth);
        textViewTilingOutlineWidth.setText(String.valueOf(tilingOutlineWidth));



        seekBarTilingQtyAxisXPlts.setProgress(tilingQtyOfTilesX);
        textViewTilingQtyAxisXPlts.setText(String.valueOf(tilingQtyOfTilesX));



        seekBarTilingQtyAxisYPlts.setProgress(tilingQtyOfTilesY);
        textViewTilingQtyAxisYPlts.setText(String.valueOf(tilingQtyOfTilesY));

        seekBarTilingBright.setProgress(tilingBrightness);
        textViewTilingBrightness.setText(String.valueOf(tilingBrightness / 100f));
    }
    private View.OnClickListener getBackBtnOCListener () {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TilingActivity.this, GeneratorsActicity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        return listener;
    }
}