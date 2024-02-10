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

    @Override
    protected void onPause () {
        super.onPause();
        seekBarDropsImSizeX = findViewById(R.id.seekBarDropsImSizeX);
        seekBarDropsImSizeY = findViewById(R.id.seekBarDropsImSizeY);
        seekBarDropsDropsQnty = findViewById(R.id.seekBarDropsDropsQnty);
        seekBarDropsDropRadius = findViewById(R.id.seekBarDropsDropRadius);
        seekBarDropsTailRadius = findViewById(R.id.seekBarDropsTailRadius);
        seekBarDropsOutlineWidth = findViewById(R.id.seekBarDropsOutlineWidth);

        int imageSizeX = seekBarDropsImSizeX.getProgress();
        int imageSizeY = seekBarDropsImSizeY.getProgress();
        int quantity = seekBarDropsDropsQnty.getProgress();
        int dropRadius = seekBarDropsDropRadius.getProgress();
        int tailRadius = seekBarDropsTailRadius.getProgress();
        int outlineWidth = seekBarDropsOutlineWidth.getProgress();




        SharedPreferences sharedPreferences = getSharedPreferences("DropsSettings", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("imageSizeX", imageSizeX);
        editor.putInt("imageSizeY", imageSizeY);
        editor.putInt("quantity", quantity);
        editor.putInt("dropRadius", dropRadius);
        editor.putInt("tailRadius", tailRadius);
        editor.putInt("outlineWidth", outlineWidth);

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

    @Override
    protected void onResume () {
        super.onResume();
        textViewDropsImWidth = findViewById(R.id.textViewDropsImWidth);
        textViewDropsImHeight = findViewById(R.id.textViewDropsImHeight);
        textViewDropsDropsQnty = findViewById(R.id.textViewDropsDropsQnty);
        textViewDropsDropsRadius = findViewById(R.id.textViewDropsDropsRadius);
        textViewDropsTailRadiusText = findViewById(R.id.textViewDropsTailRadiusText);
        textViewDropsOutlineWidth = findViewById(R.id.textViewDropsOutlineWidthText);

        seekBarDropsImSizeX = findViewById(R.id.seekBarDropsImSizeX);
        seekBarDropsImSizeY = findViewById(R.id.seekBarDropsImSizeY);
        seekBarDropsDropsQnty = findViewById(R.id.seekBarDropsDropsQnty);
        seekBarDropsDropRadius = findViewById(R.id.seekBarDropsDropRadius);
        seekBarDropsTailRadius = findViewById(R.id.seekBarDropsTailRadius);
        seekBarDropsOutlineWidth = findViewById(R.id.seekBarDropsOutlineWidth);

        seekBarDropsStartColorR = findViewById(R.id.seekBarDropsStartColorR);
        seekBarDropsStartColorG = findViewById(R.id.seekBarDropsStartColorG);
        seekBarDropsStartColorB = findViewById(R.id.seekBarDropsStartColorB);
        seekBarDropsFinishColorR = findViewById(R.id.seekBarDropsFinishColorR);
        seekBarDropsFinishColorG = findViewById(R.id.seekBarDropsFinishColorG);
        seekBarDropsFinishColorB = findViewById(R.id.seekBarDropsFinishColorB);
        seekBarDropsBackColorR = findViewById(R.id.seekBarDropsBackColorR);
        seekBarDropsBackColorG = findViewById(R.id.seekBarDropsBackColorG);
        seekBarDropsBackColorB = findViewById(R.id.seekBarDropsBackColorB);
        seekBarDropsOutlineColorR = findViewById(R.id.seekBarDropsOutlineColorR);
        seekBarDropsOutlineColorG = findViewById(R.id.seekBarDropsOutlineColorG);
        seekBarDropsOutlineColorB = findViewById(R.id.seekBarDropsOutlineColorB);

        viewDropsStartColor = findViewById(R.id.viewDropsStartColor);
        viewDropsFinishColor = findViewById(R.id.viewDropsFinishColor);
        viewDropsBackColor = findViewById(R.id.viewDropsBackColor);
        viewDropsOutlineColor = findViewById(R.id.viewDropsOutlineColor);

        SharedPreferences sharedPreferences = getSharedPreferences("DropsSettings", MODE_PRIVATE);
        int imageSizeX = sharedPreferences.getInt("imageSizeX", -1);
        int imageSizeY = sharedPreferences.getInt("imageSizeY", -1);
        int quantity = sharedPreferences.getInt("quantity", -1);
        int dropRadius = sharedPreferences.getInt("dropRadius", -1);
        int tailRadius = sharedPreferences.getInt("tailRadius", -1);
        int outlineWidth = sharedPreferences.getInt("outlineWidth", -1);

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


        if (imageSizeX != -1) {
            textViewDropsImWidth.setText(String.valueOf(imageSizeX));
            seekBarDropsImSizeX.setProgress(imageSizeX);
        }
        if (imageSizeY != -1) {
            textViewDropsImHeight.setText(String.valueOf(imageSizeY));
            seekBarDropsImSizeY.setProgress(imageSizeY);
        }
        if (quantity != -1) {
            textViewDropsDropsQnty.setText(String.valueOf(quantity));
            seekBarDropsDropsQnty.setProgress(quantity);
        }
        if (dropRadius != -1) {
            textViewDropsDropsRadius.setText(String.valueOf(dropRadius));
            seekBarDropsDropRadius.setProgress(dropRadius);
        }
        if (tailRadius != -1) {
            textViewDropsTailRadiusText.setText(String.valueOf(tailRadius));
            seekBarDropsTailRadius.setProgress(tailRadius);
        }
        if (outlineWidth != -1) {
            textViewDropsOutlineWidth.setText(String.valueOf(outlineWidth));
            seekBarDropsOutlineWidth.setProgress(outlineWidth);
        }


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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops);

        seekBarDropsImSizeX = findViewById(R.id.seekBarDropsImSizeX);
        seekBarDropsImSizeY = findViewById(R.id.seekBarDropsImSizeY);
        seekBarDropsDropsQnty = findViewById(R.id.seekBarDropsDropsQnty);
        seekBarDropsDropRadius = findViewById(R.id.seekBarDropsDropRadius);
        seekBarDropsTailRadius = findViewById(R.id.seekBarDropsTailRadius);
        seekBarDropsOutlineWidth = findViewById(R.id.seekBarDropsOutlineWidth);

        textViewDropsImWidth = findViewById(R.id.textViewDropsImWidth);
        textViewDropsImHeight = findViewById(R.id.textViewDropsImHeight);
        textViewDropsDropsQnty = findViewById(R.id.textViewDropsDropsQnty);
        textViewDropsDropsRadius = findViewById(R.id.textViewDropsDropsRadius);
        textViewDropsTailRadiusText = findViewById(R.id.textViewDropsTailRadiusText);
        textViewDropsOutlineWidthText = findViewById(R.id.textViewDropsOutlineWidthText);

//        private TextView textViewDropsImageSizeX, textViewDropsImageSizeY, textViewDropsQuantity, textViewDropsStartColor;
//        private TextView textViewDropsFinishColor, textViewDropsBackgrColor, textViewDropsDropRadius, textViewDropsTailRadius;
//        private TextView textViewDropsOutlineWidth, textViewDropsOutlineColor;

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

        textViewDropsHeader = findViewById(R.id.textViewDropsHeader);


        View.OnClickListener onClickListenerSizeX = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.image_width))
                        .setMessage(getResources().getString(R.string.explanation_size_x))
                        .show();

            }
        };
        textViewDropsImageSizeX.setOnClickListener(onClickListenerSizeX);

        View.OnClickListener onClickListenerSizeY = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.image_height))
                        .setMessage(getResources().getString(R.string.explanation_size_y))
                        .show();

            }
        };
        textViewDropsImageSizeY.setOnClickListener(onClickListenerSizeY);

        View.OnClickListener onClickListenerQuantity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.drops_quantity))
                        .setMessage(getResources().getString(R.string.explanation_drops_quantity))
                        .show();

            }
        };
        textViewDropsQuantity.setOnClickListener(onClickListenerQuantity);

        View.OnClickListener onClickListenerStartColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.start_color))
                        .setMessage(getResources().getString(R.string.explanation_start_color))
                        .show();

            }
        };
        textViewDropsStartColor.setOnClickListener(onClickListenerStartColor);

        View.OnClickListener onClickListenerFinishColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.finish_color))
                        .setMessage(getResources().getString(R.string.explanation_finish_color))
                        .show();

            }
        };
        textViewDropsFinishColor.setOnClickListener(onClickListenerFinishColor);

        View.OnClickListener onClickListenerBackgrColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.background_color))
                        .setMessage(getResources().getString(R.string.explanation_background_color))
                        .show();

            }
        };
        textViewDropsBackgrColor.setOnClickListener(onClickListenerBackgrColor);

        View.OnClickListener onClickListenerDropRadius = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.drops_radius))
                        .setMessage(getResources().getString(R.string.explanation_drops_radius))
                        .show();

            }
        };
        textViewDropsDropRadius.setOnClickListener(onClickListenerDropRadius);

        View.OnClickListener onClickListenerTailRadius = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.tail_radius))
                        .setMessage(getResources().getString(R.string.explanation_tail_radius))
                        .show();

            }
        };
        textViewDropsTailRadius.setOnClickListener(onClickListenerTailRadius);

        View.OnClickListener onClickListenerOutlineWidth = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.outline_width))
                        .setMessage(getResources().getString(R.string.explanation_outline_width))
                        .show();

            }
        };
        textViewDropsOutlineWidth.setOnClickListener(onClickListenerOutlineWidth);

        View.OnClickListener onClickListenerOutlineColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DropsActivity.this)
                        .setTitle(getResources().getString(R.string.outline_color))
                        .setMessage(getResources().getString(R.string.explanation_outline_color))
                        .show();

            }
        };
        textViewDropsOutlineColor.setOnClickListener(onClickListenerOutlineColor);


        SeekBar.OnSeekBarChangeListener seekBarChangeListenerOneBarVals = new SeekBar.OnSeekBarChangeListener() {
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
        seekBarDropsImSizeX.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);
        seekBarDropsImSizeY.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);
        seekBarDropsDropsQnty.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);
        seekBarDropsDropRadius.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);
        seekBarDropsTailRadius.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);
        seekBarDropsOutlineWidth.setOnSeekBarChangeListener(seekBarChangeListenerOneBarVals);





        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = (Button) findViewById(R.id.backButton2);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DropsActivity.this, GeneratorsActicity.class);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener);

        Button runButton = (Button)findViewById(R.id.buttonDropsRun);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runButton.setEnabled(false);
                backButton.setEnabled(false);
                ExecutorService executorService = Executors.newSingleThreadExecutor();

               int width = ((SeekBar) findViewById(R.id.seekBarDropsImSizeX)).getProgress();
               int height = ((SeekBar) findViewById(R.id.seekBarDropsImSizeY)).getProgress();
               int backCol = ((ColorDrawable) (findViewById(R.id.viewDropsBackColor)).getBackground()).getColor();

               int dropsQnty = ((SeekBar) findViewById(R.id.seekBarDropsDropsQnty)).getProgress();
               int dropRadius = ((SeekBar) findViewById(R.id.seekBarDropsDropRadius)).getProgress();
               int tailRadius = ((SeekBar) findViewById(R.id.seekBarDropsTailRadius)).getProgress();
               int startColor = ((ColorDrawable) (findViewById(R.id.viewDropsStartColor)).getBackground()).getColor();
               int finishColor = ((ColorDrawable) (findViewById(R.id.viewDropsFinishColor)).getBackground()).getColor();
               int outlineColor = ((ColorDrawable) (findViewById(R.id.viewDropsOutlineColor)).getBackground()).getColor();
               int outlineWidth =((SeekBar) findViewById(R.id.seekBarDropsOutlineWidth)).getProgress();

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
                                    startActivity(intent);
                                }
                            });
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                executorService.shutdown();
            }
        };
        runButton.setOnClickListener(onClickListener1);





        seekBarDropsStartColorR = findViewById(R.id.seekBarDropsStartColorR);
        seekBarDropsStartColorG = findViewById(R.id.seekBarDropsStartColorG);
        seekBarDropsStartColorB = findViewById(R.id.seekBarDropsStartColorB);
        viewDropsStartColor = findViewById(R.id.viewDropsStartColor);

        startColorR = seekBarDropsStartColorR.getProgress();
        startColorG = seekBarDropsStartColorG.getProgress();
        startColorB = seekBarDropsStartColorB.getProgress();

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
        seekBarDropsStartColorR.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarDropsStartColorG.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarDropsStartColorB.setOnSeekBarChangeListener(seekBarChangeListener);




        seekBarDropsFinishColorR = findViewById(R.id.seekBarDropsFinishColorR);
        seekBarDropsFinishColorG = findViewById(R.id.seekBarDropsFinishColorG);
        seekBarDropsFinishColorB = findViewById(R.id.seekBarDropsFinishColorB);
        viewDropsFinishColor = findViewById(R.id.viewDropsFinishColor);

        finishColorR = seekBarDropsFinishColorR.getProgress();
        finishColorG = seekBarDropsFinishColorG.getProgress();
        finishColorB = seekBarDropsFinishColorB.getProgress();

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
        seekBarDropsFinishColorR.setOnSeekBarChangeListener(seekBarFinishChangeListener);
        seekBarDropsFinishColorG.setOnSeekBarChangeListener(seekBarFinishChangeListener);
        seekBarDropsFinishColorB.setOnSeekBarChangeListener(seekBarFinishChangeListener);





        seekBarDropsBackColorR = findViewById(R.id.seekBarDropsBackColorR);
        seekBarDropsBackColorG = findViewById(R.id.seekBarDropsBackColorG);
        seekBarDropsBackColorB = findViewById(R.id.seekBarDropsBackColorB);
        viewDropsBackColor = findViewById(R.id.viewDropsBackColor);

        backColorR = seekBarDropsBackColorR.getProgress();
        backColorG = seekBarDropsBackColorG.getProgress();
        backColorB = seekBarDropsBackColorB.getProgress();

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
        seekBarDropsBackColorR.setOnSeekBarChangeListener(seekBarBackChangeListener);
        seekBarDropsBackColorG.setOnSeekBarChangeListener(seekBarBackChangeListener);
        seekBarDropsBackColorB.setOnSeekBarChangeListener(seekBarBackChangeListener);




        seekBarDropsOutlineColorR = findViewById(R.id.seekBarDropsOutlineColorR);
        seekBarDropsOutlineColorG = findViewById(R.id.seekBarDropsOutlineColorG);
        seekBarDropsOutlineColorB = findViewById(R.id.seekBarDropsOutlineColorB);
        viewDropsOutlineColor = findViewById(R.id.viewDropsOutlineColor);

        outlineColorR = seekBarDropsOutlineColorR.getProgress();
        outlineColorG = seekBarDropsOutlineColorG.getProgress();
        outlineColorB = seekBarDropsOutlineColorB.getProgress();

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
        seekBarDropsOutlineColorR.setOnSeekBarChangeListener(seekBarOutllineChangeListener);
        seekBarDropsOutlineColorG.setOnSeekBarChangeListener(seekBarOutllineChangeListener);
        seekBarDropsOutlineColorB.setOnSeekBarChangeListener(seekBarOutllineChangeListener);
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
}