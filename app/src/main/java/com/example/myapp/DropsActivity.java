package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.paintingtools.DropsBitmapHolder;
import com.example.paintingtools.ImageGeneratorCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DropsActivity extends AppCompatActivity {
    private SeekBar seekBarDropsStartColorR, seekBarDropsStartColorG, seekBarDropsStartColorB;
    private View viewDropsStartColor;
    private int startColorR, startColorG, startColorB;

    private SeekBar seekBarDropsFinishColorR, seekBarDropsFinishColorG, seekBarDropsFinishColorB;
    private View viewDropsFinishColor;
    private int finishColorR, finishColorG, finishColorB;

    private SeekBar seekBarDropsBackColorR, seekBarDropsBackColorG, seekBarDropsBackColorB;
    private View viewDropsBackColor;
    private int backColorR, backColorG, backColorB;

    private SeekBar seekBarDropsOutlineColorR, seekBarDropsOutlineColorG, seekBarDropsOutlineColorB;
    private View viewDropsOutlineColor;
    private int outlineColorR, outlineColorG, outlineColorB;

    private TextView textViewDropsImageSizeX, textViewDropsImageSizeY, textViewDropsQuantity, textViewDropsStartColor;
    private TextView textViewDropsFinishColor, textViewDropsBackgrColor, textViewDropsDropRadius, textViewDropsTailRadius;
    private TextView textViewDropsOutlineWidth, textViewDropsOutlineColor, textViewDropsHeader;

    private SeekBar seekBarDropsImSizeX, seekBarDropsImSizeY, seekBarDropsDropsQnty, seekBarDropsDropRadius, seekBarDropsTailRadius, seekBarDropsOutlineWidth;
    private TextView textViewDropsImWidth, textViewDropsImHeight, textViewDropsDropsQnty, textViewDropsDropsRadius, textViewDropsTailRadiusText, textViewDropsOutlineWidthText;

    int width, height, backCol, dropsQnty, dropRadius, tailRadius, startColor, finishColor, outlineColor, outlineWidth;
    int imageSizeXPR, imageSizeYPR, quantityPR, dropRadiusPR, tailRadiusPR, outlineWidthPR;
    SharedPreferences sharedPreferences;

    int imageSizeX_onPause, imageSizeY_onPause, quantity_onPause, dropRadius_onPause, tailRadius_onPause, outlineWidth_onPause;


    @Override
    protected void onPause () {
        super.onPause();

        getOnPauseValues();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        putOnPauseValues(editor);
    }

    @Override
    protected void onResume () {
        super.onResume();

        getPreferences(sharedPreferences);
        getColorsPreferences(sharedPreferences);
        resumePreferences();
        resumeColorsPreferences();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("DropsSettings", MODE_PRIVATE);
        setContentView(R.layout.activity_drops);
        textViewDropsHeader = findViewById(R.id.textViewDropsHeader);

        getSeekBarsOneBar();
        getTextViewsValues();
        getTextViewsLabels();
        setOnClickListenerLabels();
        setSeekBarOneBarChangeListener();

        getForStartColor();
        setStartColorsOC();
        getForFinishColor();
        setFinishColorsOC();
        getForBackColor();
        setBackColorsOC();
        getForOutlineColor();
        setOutlineColorsOC();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.backButton2);
        backButton.setOnClickListener(getBackButtonListener());

        Button runButton = (Button)findViewById(R.id.buttonDropsRun);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dropsIsGenRunning = sharedPreferences.getBoolean("dropsIsGenRunning", false);
                if (dropsIsGenRunning) {
                    return;
                }
                sharedPreferences.edit().putBoolean("dropsIsGenRunning", true);

                runButton.setEnabled(false);
                backButton.setEnabled(false);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                setRunValues();

                Callable<Bitmap> imageGeneratorCallable = new ImageGeneratorCallable(
                       dropsQnty,
                       dropRadius,
                       tailRadius,
                       startColor,
                       finishColor,
                       outlineColor,
                       outlineWidth,
                       true,
                       width,
                       height,
                       backCol);
                Future<Bitmap> futureResult = executorService.submit(imageGeneratorCallable);

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap image = futureResult.get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DropsBitmapHolder.setBitmap(image);
                                    runButton.setEnabled(true);
                                    Intent intent = new Intent(DropsActivity.this, DropsPictureActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sharedPreferences.edit().putBoolean("dropsIsGenRunning", false);
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
                executorService.shutdown();
            }
        };
        runButton.setOnClickListener(onClickListener1);


        seekBarDropsStartColorR.setOnSeekBarChangeListener(getSeekBarStartColorListener());
        seekBarDropsStartColorG.setOnSeekBarChangeListener(getSeekBarStartColorListener());
        seekBarDropsStartColorB.setOnSeekBarChangeListener(getSeekBarStartColorListener());
        seekBarDropsFinishColorR.setOnSeekBarChangeListener(getSeekBarFinishColorListener());
        seekBarDropsFinishColorG.setOnSeekBarChangeListener(getSeekBarFinishColorListener());
        seekBarDropsFinishColorB.setOnSeekBarChangeListener(getSeekBarFinishColorListener());
        seekBarDropsBackColorR.setOnSeekBarChangeListener(getSeekBarBackColorListener());
        seekBarDropsBackColorG.setOnSeekBarChangeListener(getSeekBarBackColorListener());
        seekBarDropsBackColorB.setOnSeekBarChangeListener(getSeekBarBackColorListener());
        seekBarDropsOutlineColorR.setOnSeekBarChangeListener(getSeekBarOutlineColorListener());
        seekBarDropsOutlineColorG.setOnSeekBarChangeListener(getSeekBarOutlineColorListener());
        seekBarDropsOutlineColorB.setOnSeekBarChangeListener(getSeekBarOutlineColorListener());
    }

    private void updateColorIndicatorStart() {
        int color = Color.rgb(startColorR, startColorG, startColorB);
        viewDropsStartColor.setBackgroundColor(color);
    }
    private void updateFinishColorIndicatorStart() {
        int color = Color.rgb(finishColorR, finishColorG, finishColorB);
        viewDropsFinishColor.setBackgroundColor(color);
    }
    private void updateBackColorIndicatorStart() {
        int color = Color.rgb(backColorR, backColorG, backColorB);
        viewDropsBackColor.setBackgroundColor(color);
    }
    private void updateOutlineColorIndicatorStart() {
        int color = Color.rgb(outlineColorR, outlineColorG, outlineColorB);
        viewDropsOutlineColor.setBackgroundColor(color);
    }

    private void getSeekBarsOneBar () {
        seekBarDropsImSizeX = findViewById(R.id.seekBarDropsImSizeX);
        seekBarDropsImSizeY = findViewById(R.id.seekBarDropsImSizeY);
        seekBarDropsDropsQnty = findViewById(R.id.seekBarDropsDropsQnty);
        seekBarDropsDropRadius = findViewById(R.id.seekBarDropsDropRadius);
        seekBarDropsTailRadius = findViewById(R.id.seekBarDropsTailRadius);
        seekBarDropsOutlineWidth = findViewById(R.id.seekBarDropsOutlineWidth);
    }
    private void getTextViewsValues () {
        textViewDropsImWidth = findViewById(R.id.textViewDropsImWidth);
        textViewDropsImHeight = findViewById(R.id.textViewDropsImHeight);
        textViewDropsDropsQnty = findViewById(R.id.textViewDropsDropsQnty);
        textViewDropsDropsRadius = findViewById(R.id.textViewDropsDropsRadius);
        textViewDropsTailRadiusText = findViewById(R.id.textViewDropsTailRadiusText);
        textViewDropsOutlineWidthText = findViewById(R.id.textViewDropsOutlineWidthText);
    }
    private void getTextViewsLabels () {
        textViewDropsImageSizeX = findViewById(R.id.textViewDropsImageSizeX);
        textViewDropsImageSizeY = findViewById(R.id.textViewDropsImageSizeY);
        textViewDropsQuantity = findViewById(R.id.textViewDropsQuantity);
        textViewDropsStartColor = findViewById(R.id.textViewDropsStartColor);
        textViewDropsFinishColor = findViewById(R.id.textViewDropsFinishColor);
        textViewDropsBackgrColor = findViewById(R.id.textViewDropsBackgrColor);
        textViewDropsDropRadius = findViewById(R.id.textViewDropsDropRadius);
        textViewDropsTailRadius = findViewById(R.id.textViewDropsTailRadius);
        textViewDropsOutlineWidth = findViewById(R.id.textViewDropsOutlineWidth);
        textViewDropsOutlineColor = findViewById(R.id.textViewDropsOutlineColor);
    }
    private View.OnClickListener getLabelsOnClickListener (String title, String message) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .show();
            }
        };
        return listener;
    }
    private void setOnClickListenerLabels () {
        textViewDropsImageSizeX.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.image_width), getResources().getString(R.string.explanation_size_x)));
        textViewDropsImageSizeY.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.image_height), getResources().getString(R.string.explanation_size_y)));
        textViewDropsQuantity.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.drops_quantity), getResources().getString(R.string.explanation_drops_quantity)));
        textViewDropsStartColor.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.start_color), getResources().getString(R.string.explanation_start_color)));
        textViewDropsFinishColor.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.finish_color), getResources().getString(R.string.explanation_finish_color)));
        textViewDropsBackgrColor.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.background_color), getResources().getString(R.string.explanation_background_color)));
        textViewDropsDropRadius.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.drops_radius), getResources().getString(R.string.explanation_drops_radius)));
        textViewDropsTailRadius.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.tail_radius), getResources().getString(R.string.explanation_tail_radius)));
        textViewDropsOutlineWidth.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.outline_width), getResources().getString(R.string.explanation_outline_width)));
        textViewDropsOutlineColor.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.outline_color), getResources().getString(R.string.explanation_outline_color)));
        textViewDropsHeader.setOnClickListener(getLabelsOnClickListener(getResources().getString(R.string.label_drops_generator), getResources().getString(R.string.explanation_drops_generator)));
    }
    private SeekBar.OnSeekBarChangeListener getSeekbarOneBarChangeListener () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {;
                if (seekBar.getId() == seekBarDropsImSizeX.getId()) {
                    textViewDropsImWidth.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarDropsImSizeY.getId()){
                    textViewDropsImHeight.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarDropsDropsQnty.getId()) {
                    textViewDropsDropsQnty.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarDropsDropRadius.getId()) {
                    textViewDropsDropsRadius.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarDropsTailRadius.getId()) {
                    textViewDropsTailRadiusText.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarDropsOutlineWidth.getId()) {
                    textViewDropsOutlineWidthText.setText(String.valueOf(progress));
                }
                updateColorIndicatorStart();
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
    private void setSeekBarOneBarChangeListener () {
        seekBarDropsImSizeX.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
        seekBarDropsImSizeY.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
        seekBarDropsDropsQnty.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
        seekBarDropsDropRadius.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
        seekBarDropsTailRadius.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
        seekBarDropsOutlineWidth.setOnSeekBarChangeListener(getSeekbarOneBarChangeListener());
    }
    private View.OnClickListener getBackButtonListener () {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DropsActivity.this, GeneratorsActicity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        return onClickListener;
    }
    private void setRunValues () {
        width = ((SeekBar) findViewById(R.id.seekBarDropsImSizeX)).getProgress();
        height = ((SeekBar) findViewById(R.id.seekBarDropsImSizeY)).getProgress();
        backCol = ((ColorDrawable) (findViewById(R.id.viewDropsBackColor)).getBackground()).getColor();

        dropsQnty = ((SeekBar) findViewById(R.id.seekBarDropsDropsQnty)).getProgress();
        dropRadius = ((SeekBar) findViewById(R.id.seekBarDropsDropRadius)).getProgress();
        tailRadius = ((SeekBar) findViewById(R.id.seekBarDropsTailRadius)).getProgress();
        startColor = ((ColorDrawable) (findViewById(R.id.viewDropsStartColor)).getBackground()).getColor();
        finishColor = ((ColorDrawable) (findViewById(R.id.viewDropsFinishColor)).getBackground()).getColor();
        outlineColor = ((ColorDrawable) (findViewById(R.id.viewDropsOutlineColor)).getBackground()).getColor();
        outlineWidth =((SeekBar) findViewById(R.id.seekBarDropsOutlineWidth)).getProgress();
    }
    private void getForStartColor () {
        seekBarDropsStartColorR = findViewById(R.id.seekBarDropsStartColorR);
        seekBarDropsStartColorG = findViewById(R.id.seekBarDropsStartColorG);
        seekBarDropsStartColorB = findViewById(R.id.seekBarDropsStartColorB);
        viewDropsStartColor = findViewById(R.id.viewDropsStartColor);
    }
    private void getForFinishColor () {
        seekBarDropsFinishColorR = findViewById(R.id.seekBarDropsFinishColorR);
        seekBarDropsFinishColorG = findViewById(R.id.seekBarDropsFinishColorG);
        seekBarDropsFinishColorB = findViewById(R.id.seekBarDropsFinishColorB);
        viewDropsFinishColor = findViewById(R.id.viewDropsFinishColor);
    }
    private void getForBackColor () {
        seekBarDropsBackColorR = findViewById(R.id.seekBarDropsBackColorR);
        seekBarDropsBackColorG = findViewById(R.id.seekBarDropsBackColorG);
        seekBarDropsBackColorB = findViewById(R.id.seekBarDropsBackColorB);
        viewDropsBackColor = findViewById(R.id.viewDropsBackColor);
    }
    private void getForOutlineColor () {
        seekBarDropsOutlineColorR = findViewById(R.id.seekBarDropsOutlineColorR);
        seekBarDropsOutlineColorG = findViewById(R.id.seekBarDropsOutlineColorG);
        seekBarDropsOutlineColorB = findViewById(R.id.seekBarDropsOutlineColorB);
        viewDropsOutlineColor = findViewById(R.id.viewDropsOutlineColor);
    }
    private void setStartColorsOC () {
        startColorR = seekBarDropsStartColorR.getProgress();
        startColorG = seekBarDropsStartColorG.getProgress();
        startColorB = seekBarDropsStartColorB.getProgress();
    }
    private void setFinishColorsOC () {
        finishColorR = seekBarDropsFinishColorR.getProgress();
        finishColorG = seekBarDropsFinishColorG.getProgress();
        finishColorB = seekBarDropsFinishColorB.getProgress();
    }
    private void setBackColorsOC () {
        backColorR = seekBarDropsBackColorR.getProgress();
        backColorG = seekBarDropsBackColorG.getProgress();
        backColorB = seekBarDropsBackColorB.getProgress();
    }
    private void setOutlineColorsOC () {
        outlineColorR = seekBarDropsOutlineColorR.getProgress();
        outlineColorG = seekBarDropsOutlineColorG.getProgress();
        outlineColorB = seekBarDropsOutlineColorB.getProgress();
    }
    private SeekBar.OnSeekBarChangeListener getSeekBarStartColorListener () {
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {;
                if (seekBar.getId() == seekBarDropsStartColorR.getId()) {
                    startColorR = progress;
                    System.out.println(startColorR);
                }
                else if (seekBar.getId() == seekBarDropsStartColorG.getId()){
                    startColorG = progress;
                }
                else if (seekBar.getId() == seekBarDropsStartColorB.getId()){
                    startColorB = progress;
                }
                updateColorIndicatorStart();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        return seekBarChangeListener;
    }
    private SeekBar.OnSeekBarChangeListener getSeekBarFinishColorListener () {
        SeekBar.OnSeekBarChangeListener seekBarFinishChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {;
                if (seekBar.getId() == seekBarDropsFinishColorR.getId()) {
                    finishColorR = progress;
                }
                else if (seekBar.getId() == seekBarDropsFinishColorG.getId()){
                    finishColorG = progress;
                }
                else if (seekBar.getId() == seekBarDropsFinishColorB.getId()){
                    finishColorB = progress;
                }
                updateFinishColorIndicatorStart();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        return seekBarFinishChangeListener;
    }
    private SeekBar.OnSeekBarChangeListener getSeekBarBackColorListener () {
        SeekBar.OnSeekBarChangeListener seekBarBackChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {;
                if (seekBar.getId() == seekBarDropsBackColorR.getId()) {
                    backColorR = progress;
                }
                else if (seekBar.getId() == seekBarDropsBackColorG.getId()){
                    backColorG = progress;
                }
                else if (seekBar.getId() == seekBarDropsBackColorB.getId()){
                    backColorB = progress;
                }
                updateBackColorIndicatorStart();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        return seekBarBackChangeListener;
    }
    private SeekBar.OnSeekBarChangeListener getSeekBarOutlineColorListener () {
        SeekBar.OnSeekBarChangeListener seekBarOutllineChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {;
                if (seekBar.getId() == seekBarDropsOutlineColorR.getId()) {
                    outlineColorR = progress;
                }
                else if (seekBar.getId() == seekBarDropsOutlineColorG.getId()){
                    outlineColorG = progress;
                }
                else if (seekBar.getId() == seekBarDropsOutlineColorB.getId()){
                    outlineColorB = progress;
                }
                updateOutlineColorIndicatorStart();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        return seekBarOutllineChangeListener;
    }
    private void getPreferences (SharedPreferences sharedPreferences) {
        imageSizeXPR = sharedPreferences.getInt("imageSizeX", -1);
        imageSizeYPR = sharedPreferences.getInt("imageSizeY", -1);
        quantityPR = sharedPreferences.getInt("quantity", -1);
        dropRadiusPR = sharedPreferences.getInt("dropRadius", -1);
        tailRadiusPR = sharedPreferences.getInt("tailRadius", -1);
        outlineWidthPR = sharedPreferences.getInt("outlineWidth", -1);
    }
    private void getColorsPreferences (SharedPreferences sharedPreferences) {
        startColorR = sharedPreferences.getInt("startColorR", -1);
        startColorG = sharedPreferences.getInt("startColorG", -1);
        startColorB = sharedPreferences.getInt("startColorB", -1);
        finishColorR = sharedPreferences.getInt("finishColorR", -1);
        finishColorG = sharedPreferences.getInt("finishColorG", -1);
        finishColorB = sharedPreferences.getInt("finishColorB", -1);
        backColorR = sharedPreferences.getInt("backColorR", -1);
        backColorG = sharedPreferences.getInt("backColorG", -1);
        backColorB = sharedPreferences.getInt("backColorB", -1);
        outlineColorR = sharedPreferences.getInt("outlineColorR", -1);
        outlineColorG = sharedPreferences.getInt("outlineColorG", -1);
        outlineColorB = sharedPreferences.getInt("outlineColorB", -1);
    }
    private void resumePreferences () {
        if (imageSizeXPR != -1) {
            textViewDropsImWidth.setText(String.valueOf(imageSizeXPR));
            seekBarDropsImSizeX.setProgress(imageSizeXPR);
        }
        if (imageSizeYPR != -1) {
            textViewDropsImHeight.setText(String.valueOf(imageSizeYPR));
            seekBarDropsImSizeY.setProgress(imageSizeYPR);
        }
        if (quantityPR != -1) {
            textViewDropsDropsQnty.setText(String.valueOf(quantityPR));
            seekBarDropsDropsQnty.setProgress(quantityPR);
        }
        if (dropRadiusPR != -1) {
            textViewDropsDropsRadius.setText(String.valueOf(dropRadiusPR));
            seekBarDropsDropRadius.setProgress(dropRadiusPR);
        }
        if (tailRadiusPR != -1) {
            textViewDropsTailRadiusText.setText(String.valueOf(tailRadiusPR));
            seekBarDropsTailRadius.setProgress(tailRadiusPR);
        }
        if (outlineWidthPR != -1) {
            textViewDropsOutlineWidthText.setText(String.valueOf(outlineWidthPR));
            seekBarDropsOutlineWidth.setProgress(outlineWidthPR);
        }
    }
    private void resumeColorsPreferences () {
        if (startColorR != -1) {
            seekBarDropsStartColorR.setProgress(startColorR);
            updateColorIndicatorStart();
        }
        if (startColorG != -1) {
            seekBarDropsStartColorG.setProgress(startColorG);
            updateColorIndicatorStart();
        }
        if (startColorB != -1) {
            seekBarDropsStartColorB.setProgress(startColorB);
            updateColorIndicatorStart();
        }
        if (finishColorR != -1) {
            seekBarDropsFinishColorR.setProgress(finishColorR);
            updateFinishColorIndicatorStart();
        }
        if (finishColorG != -1) {
            seekBarDropsFinishColorG.setProgress(finishColorG);
            updateFinishColorIndicatorStart();
        }
        if (finishColorB != -1) {
            seekBarDropsFinishColorB.setProgress(finishColorB);
            updateFinishColorIndicatorStart();
        }
        if (backColorR != -1) {
            seekBarDropsBackColorR.setProgress(backColorR);
            updateBackColorIndicatorStart();
        }
        if (backColorG != -1) {
            seekBarDropsBackColorG.setProgress(backColorG);
            updateBackColorIndicatorStart();
        }
        if (backColorB != -1) {
            seekBarDropsBackColorB.setProgress(backColorB);
            updateBackColorIndicatorStart();
        }
        if (outlineColorR != -1) {
            seekBarDropsOutlineColorR.setProgress(outlineColorR);
            updateOutlineColorIndicatorStart();
        }
        if (outlineColorG != -1) {
            seekBarDropsOutlineColorG.setProgress(outlineColorG);
            updateOutlineColorIndicatorStart();
        }
        if (outlineColorB != -1) {
            seekBarDropsOutlineColorB.setProgress(outlineColorB);
            updateOutlineColorIndicatorStart();
        }
    }
    private void getOnPauseValues () {
        imageSizeX_onPause = seekBarDropsImSizeX.getProgress();
        imageSizeY_onPause = seekBarDropsImSizeY.getProgress();
        quantity_onPause = seekBarDropsDropsQnty.getProgress();
        dropRadius_onPause = seekBarDropsDropRadius.getProgress();
        tailRadius_onPause = seekBarDropsTailRadius.getProgress();
        outlineWidth_onPause = seekBarDropsOutlineWidth.getProgress();
    }
    private void putOnPauseValues (SharedPreferences.Editor editor) {
        editor.putInt("imageSizeX", imageSizeX_onPause);
        editor.putInt("imageSizeY", imageSizeY_onPause);
        editor.putInt("quantity", quantity_onPause);
        editor.putInt("dropRadius", dropRadius_onPause);
        editor.putInt("tailRadius", tailRadius_onPause);
        editor.putInt("outlineWidth", outlineWidth_onPause);

        editor.putInt("startColorR", startColorR);
        editor.putInt("startColorG", startColorG);
        editor.putInt("startColorB", startColorB);
        editor.putInt("finishColorR", finishColorR);
        editor.putInt("finishColorG", finishColorG);
        editor.putInt("finishColorB", finishColorB);
        editor.putInt("backColorR", backColorR);
        editor.putInt("backColorG", backColorG);
        editor.putInt("backColorB", backColorB);
        editor.putInt("outlineColorR", outlineColorR);
        editor.putInt("outlineColorG", outlineColorG);
        editor.putInt("outlineColorB", outlineColorB);
        editor.apply();
    }
}