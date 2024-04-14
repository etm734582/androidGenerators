package com.example.myapp;

import androidx.annotation.CallSuper;
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

import com.example.paintingtools.CurlsBitmapHolder;
import com.example.paintingtools.CurlsGeneratorCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CurlsActivity extends AppCompatActivity {

    private SeekBar seekBarCurlsImSizeX, seekBarCurlsImSizeY, seekBarCurlsStepsQnty, seekBarCurlsBranchLen, seekBarCurlsTreesQnty;
    private SeekBar seekBarCurlsAngleShiftLim, seekBarCurlsAngleShiftPlus, seekBarCurlsLineWidth, seekBarLineToNewLine;
    private TextView textViewCurlsImWidth, textViewCurlsImHeight, textViewCurlsSteps, textViewCurlsBranchLenght;
    private TextView textViewCurlsAShiftMax, textViewAShiftPlus, textViewCurlsLineWidthL, textViewLineToNewLine, textViewCurlsTreesQntyCntr;

    private TextView textViewCurlsmageSizeX, textViewCurlsImageSizeY, textViewCurlsStepsQnty, textViewCurlsBranchLen;
    private TextView textViewCurlsAngleShiftMax, textViewAngleShiftPlus, textViewCurlsLineWidth, textViewCurlsStartColor;
    private TextView textViewCurlsFinishColor, textViewCurlsBackColor, textViewStarttoNewLine, textViewCurlsTreesQnty;
    private TextView textViewCurlsHeader;

    private SeekBar seekBarCurlsStartColorR, seekBarCurlsStartColorG, seekBarCurlsStartColorB;
    private View viewCurlsStartColor;
    private int startColorR, startColorG, startColorB;

    private SeekBar seekBarCurlsFinishColorR, seekBarCurlsFinishColorG, seekBarCurlsFinishColorB;
    private View viewCurlsFinishColor;
    private int finishColorR, finishColorG, finishColorB;

    private SeekBar seekBarCurlsBackColR, seekBarCurlsBackColG, seekBarCurlsBackColB;
    private View viewCurlsBackColor;
    private int backColorR, backColorG, backColorB;

    private int curlsImWidth, curlsImheight, curlsSteps, curlsBranchLenght, curlsAShiftMax, curlsAShiftPlus, curlsLineWidthL, curlsLineToNewLine, curlsTreesQntyCntr;
    SharedPreferences sharedPreferences;

    @Override
    protected void onPause() {
        super.onPause();

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        putOnPauseValues(editor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getColors(sharedPreferences);
        getSettingsPreferences(sharedPreferences);
        resumeColors();
        resumePreferences();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curls);

        sharedPreferences = getSharedPreferences("CurlsSettings", MODE_PRIVATE);
        textViewCurlsHeader = findViewById(R.id.textViewCurlsHeader);
        getSeekBarsOneBar();
        getTextViews();
        getForStartColor();
        getForFinishColor();
        getForBackColor();
        getDefaultColors();
        getParametersName();

        SeekBar.OnSeekBarChangeListener listenerForOneBarParameters = getListenerForOneBarParameters();
        setListenerForOneBarSeekBar (listenerForOneBarParameters);
        Button backButton = (Button) findViewById(R.id.buttonCurlsBack);
        backButton.setOnClickListener(getBackButtonListener());

        setInfoOnClickListeners();
        setColorsSeekBarOnChangeListener();

        Button runButton = (Button) findViewById(R.id.buttonCurlsRun);
        View.OnClickListener onClickListenerRun = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean curlsIsGenRunning = sharedPreferences.getBoolean("curlsIsGenRunning", false);
                if (curlsIsGenRunning) {
                    return;
                }
                sharedPreferences.edit().putBoolean("curlsIsGenRunning", true);

                runButton.setEnabled(false);
                backButton.setEnabled(false);
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                Callable<Bitmap> curlsGeneratorCallable = new CurlsGeneratorCallable(
                        seekBarCurlsStepsQnty.getProgress(),
                        seekBarLineToNewLine.getProgress(),
                        ((ColorDrawable) viewCurlsStartColor.getBackground()).getColor(),
                        ((ColorDrawable) viewCurlsFinishColor.getBackground()).getColor(),
                        seekBarCurlsBranchLen.getProgress(),
                        seekBarCurlsAngleShiftLim.getProgress(),
                        seekBarCurlsAngleShiftPlus.getProgress(),
                        seekBarCurlsLineWidth.getProgress(),
                        true,
                        ((ColorDrawable) viewCurlsBackColor.getBackground()).getColor(),
                        seekBarCurlsImSizeX.getProgress(),
                        seekBarCurlsImSizeY.getProgress(),
                        seekBarCurlsTreesQnty.getProgress());
                Future<Bitmap> futureResult = executorService.submit(curlsGeneratorCallable);

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Bitmap image = futureResult.get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CurlsBitmapHolder.setBitmap(image);
                                    runButton.setEnabled(true);
                                    backButton.setEnabled(true);
                                    Intent intent = new Intent(CurlsActivity.this, CurlsPictureActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sharedPreferences.edit().putBoolean("curlsIsGenRunning", false);
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
            }
        };
        runButton.setOnClickListener(onClickListenerRun);
    }

    private void updateColorIndicatorStart() {
        int color = Color.rgb(startColorR, startColorG, startColorB);
        viewCurlsStartColor.setBackgroundColor(color);
    }
    private void updateColorIndicatorFinish() {
        int color = Color.rgb(finishColorR, finishColorG, finishColorB);
        viewCurlsFinishColor.setBackgroundColor(color);
    }
    private void updateBackColorIndicatorBack() {
        int color = Color.rgb(backColorR, backColorG, backColorB);
        viewCurlsBackColor.setBackgroundColor(color);
    }
    private void getSeekBarsOneBar () {
        seekBarCurlsImSizeX = findViewById(R.id.seekBarCurlsImSizeX);
        seekBarCurlsImSizeY = findViewById(R.id.seekBarCurlsImSizeY);
        seekBarCurlsStepsQnty = findViewById(R.id.seekBarCurlsStepsQnty);
        seekBarCurlsBranchLen = findViewById(R.id.seekBarCurlsBranchLen);
        seekBarCurlsAngleShiftLim = findViewById(R.id.seekBarCurlsAngleShiftLim);
        seekBarCurlsAngleShiftPlus = findViewById(R.id.seekBarCurlsAngleShiftPlus);
        seekBarCurlsLineWidth = findViewById(R.id.seekBarCurlsLineWidth);
        seekBarLineToNewLine = findViewById(R.id.seekBarLineToNewLine);
        seekBarCurlsTreesQnty = findViewById(R.id.seekBarCurlsTreesQnty);
    }
    private void getTextViews () {
        textViewCurlsImWidth = findViewById(R.id.textViewCurlsImWidth);
        textViewCurlsImHeight = findViewById(R.id.textViewCurlsImHeight);
        textViewCurlsSteps = findViewById(R.id.textViewCurlsSteps);
        textViewCurlsBranchLenght = findViewById(R.id.textViewCurlsBranchLenght);
        textViewCurlsAShiftMax = findViewById(R.id.textViewCurlsAShiftMax);
        textViewAShiftPlus = findViewById(R.id.textViewAShiftPlus);
        textViewCurlsLineWidthL = findViewById(R.id.textViewCurlsLineWidthL);
        textViewLineToNewLine = findViewById(R.id.textViewLineToNewLine);
        textViewCurlsTreesQntyCntr = findViewById(R.id.textViewCurlsTreesQntyCntr);
    }
    private void getForStartColor () {
        seekBarCurlsStartColorR = findViewById(R.id.seekBarCurlsStartColorR);
        seekBarCurlsStartColorG = findViewById(R.id.seekBarCurlsStartColorG);
        seekBarCurlsStartColorB = findViewById(R.id.seekBarCurlsStartColorB);
        viewCurlsStartColor = findViewById(R.id.viewCurlsStartColor);
    }
    private void getForFinishColor () {
        seekBarCurlsFinishColorR = findViewById(R.id.seekBarCurlsFinishColorR);
        seekBarCurlsFinishColorG = findViewById(R.id.seekBarCurlsFinishColorG);
        seekBarCurlsFinishColorB = findViewById(R.id.seekBarCurlsFinishColorB);
        viewCurlsFinishColor = findViewById(R.id.viewCurlsFinishColor);
    }
    private void getForBackColor () {
        seekBarCurlsBackColR = findViewById(R.id.seekBarCurlsBackColR);
        seekBarCurlsBackColG = findViewById(R.id.seekBarCurlsBackColG);
        seekBarCurlsBackColB = findViewById(R.id.seekBarCurlsBackColB);
        viewCurlsBackColor = findViewById(R.id.viewCurlsBackColor);
    }
    private void getDefaultColors () {
        startColorR = 100;
        startColorG = 100;
        startColorB = 100;
        finishColorR = 100;
        finishColorG = 100;
        finishColorB = 100;
        backColorR = 100;
        backColorG = 100;
        backColorB = 100;
    }
    private void getParametersName () {
        textViewCurlsmageSizeX = findViewById(R.id.textViewCurlsmageSizeX);
        textViewCurlsImageSizeY = findViewById(R.id.textViewCurlsImageSizeY);
        textViewCurlsStepsQnty = findViewById(R.id.textViewCurlsStepsQnty);
        textViewCurlsBranchLen = findViewById(R.id.textViewCurlsBranchLen);
        textViewCurlsAngleShiftMax = findViewById(R.id.textViewCurlsAngleShiftMax);
        textViewAngleShiftPlus = findViewById(R.id.textViewAngleShiftPlus);
        textViewCurlsLineWidth = findViewById(R.id.textViewCurlsLineWidth);
        textViewCurlsStartColor = findViewById(R.id.textViewCurlsStartColor);
        textViewCurlsFinishColor = findViewById(R.id.textViewCurlsFinishColor);
        textViewCurlsBackColor = findViewById(R.id.textViewCurlsBackColor);
        textViewStarttoNewLine = findViewById(R.id.textViewStarttoNewLine);
        textViewCurlsTreesQnty = findViewById(R.id.textViewCurlsTreesQnty);
    }
    private View.OnClickListener getOnClickListener (String title, String message) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .show();
            }
        };
        return onClickListener;
    }
    private void setInfoOnClickListeners () {
        textViewCurlsmageSizeX.setOnClickListener(getOnClickListener(getResources().getString(R.string.image_width), getResources().getString(R.string.explanation_size_x)));
        textViewCurlsImageSizeY.setOnClickListener(getOnClickListener(getResources().getString(R.string.image_height), getResources().getString(R.string.explanation_size_y)));
        textViewCurlsStepsQnty.setOnClickListener(getOnClickListener(getResources().getString(R.string.steps_qnty), getResources().getString(R.string.explanation_steps_qnty)));
        textViewCurlsBranchLen.setOnClickListener(getOnClickListener(getResources().getString(R.string.branch_lenght), getResources().getString(R.string.explanation_branch_lenght)));
        textViewCurlsAngleShiftMax.setOnClickListener(getOnClickListener(getResources().getString(R.string.angle_shift_max), getResources().getString(R.string.explanation_angle_shift_max)));
        textViewAngleShiftPlus.setOnClickListener(getOnClickListener(getResources().getString(R.string.angle_shift_plus), getResources().getString(R.string.explanation_angle_shift_plus)));
        textViewCurlsStartColor.setOnClickListener(getOnClickListener(getResources().getString(R.string.start_color), getResources().getString(R.string.explanation_start_color)));
        textViewCurlsFinishColor.setOnClickListener(getOnClickListener(getResources().getString(R.string.finish_color), getResources().getString(R.string.explanation_finish_color)));
        textViewCurlsBackColor.setOnClickListener(getOnClickListener(getResources().getString(R.string.background_color), getResources().getString(R.string.explanation_background_color)));
        textViewStarttoNewLine.setOnClickListener(getOnClickListener(getResources().getString(R.string.lines_to_new_line), getResources().getString(R.string.explanation_LTNL)));
        textViewCurlsTreesQnty.setOnClickListener(getOnClickListener(getResources().getString(R.string.trees_qnty_exp), getResources().getString(R.string.explanation_trees_qnty)));
        textViewCurlsHeader.setOnClickListener(getOnClickListener(getResources().getString(R.string.curls_generator), getResources().getString(R.string.explanation_curls_generator)));
        textViewCurlsLineWidth.setOnClickListener(getOnClickListener(getResources().getString(R.string.line_width), getResources().getString(R.string.explanation_line_width)));
    }
    private void setColorsSeekBarOnChangeListener () {
        seekBarCurlsStartColorR.setOnSeekBarChangeListener(getSeekBarStartColorListener());
        seekBarCurlsStartColorG.setOnSeekBarChangeListener(getSeekBarStartColorListener());
        seekBarCurlsStartColorB.setOnSeekBarChangeListener(getSeekBarStartColorListener());

        seekBarCurlsFinishColorR.setOnSeekBarChangeListener(getSeekBarFinishColorListener());
        seekBarCurlsFinishColorG.setOnSeekBarChangeListener(getSeekBarFinishColorListener());
        seekBarCurlsFinishColorB.setOnSeekBarChangeListener(getSeekBarFinishColorListener());

        seekBarCurlsBackColR.setOnSeekBarChangeListener(getSeekBarBackColorListener());
        seekBarCurlsBackColG.setOnSeekBarChangeListener(getSeekBarBackColorListener());
        seekBarCurlsBackColB.setOnSeekBarChangeListener(getSeekBarBackColorListener());
    }
    private void getColors (SharedPreferences sharedPreferences) {
        startColorR = sharedPreferences.getInt("startColorR", -1);
        startColorG = sharedPreferences.getInt("startColorG", -1);
        startColorB = sharedPreferences.getInt("startColorB", -1);
        finishColorR = sharedPreferences.getInt("finishColorR", -1);
        finishColorG = sharedPreferences.getInt("finishColorG", -1);
        finishColorB = sharedPreferences.getInt("finishColorB", -1);
        backColorR = sharedPreferences.getInt("backColorR", -1);
        backColorG = sharedPreferences.getInt("backColorG", -1);
        backColorB = sharedPreferences.getInt("backColorB", -1);
    }

    private SeekBar.OnSeekBarChangeListener getListenerForOneBarParameters () {
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBarCurlsImSizeX.getId()) {
                    textViewCurlsImWidth.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsImSizeY.getId()) {
                    textViewCurlsImHeight.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsLineWidth.getId()) {
                    textViewCurlsLineWidthL.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsStepsQnty.getId()) {
                    textViewCurlsSteps.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsBranchLen.getId()) {
                    textViewCurlsBranchLenght.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsAngleShiftLim.getId()) {
                    textViewCurlsAShiftMax.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsAngleShiftPlus.getId()) {
                    textViewAShiftPlus.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarLineToNewLine.getId()) {
                    textViewLineToNewLine.setText(String.valueOf(progress));
                }
                else if (seekBar.getId() == seekBarCurlsTreesQnty.getId()) {
                    textViewCurlsTreesQntyCntr.setText(String.valueOf(progress));
                }
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
    private View.OnClickListener getBackButtonListener () {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurlsActivity.this, GeneratorsActicity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        return onClickListener;
    }
    private SeekBar.OnSeekBarChangeListener getSeekBarStartColorListener () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBarCurlsStartColorR.getId()) {
                    startColorR = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsStartColorG.getId()) {
                    startColorG = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsStartColorB.getId()) {
                    startColorB = seekBar.getProgress();
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

    private SeekBar.OnSeekBarChangeListener getSeekBarFinishColorListener () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBarCurlsFinishColorR.getId()) {
                    finishColorR = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsFinishColorG.getId()) {
                    finishColorG = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsFinishColorB.getId()) {
                    finishColorB = seekBar.getProgress();
                }
                updateColorIndicatorFinish();
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

    private SeekBar.OnSeekBarChangeListener getSeekBarBackColorListener () {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == seekBarCurlsBackColR.getId()) {
                    backColorR = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsBackColG.getId()) {
                    backColorG = seekBar.getProgress();
                }
                if (seekBar.getId() == seekBarCurlsBackColB.getId()) {
                    backColorB = seekBar.getProgress();
                }
                updateBackColorIndicatorBack();
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
    private void getSettingsPreferences (SharedPreferences sharedPreferences) {
        curlsImWidth = sharedPreferences.getInt("curlsImWidth", -1);
        curlsImheight = sharedPreferences.getInt("curlsImheight", -1);
        curlsSteps = sharedPreferences.getInt("curlsSteps", -1);
        curlsBranchLenght = sharedPreferences.getInt("curlsBranchLenght", -1);
        curlsAShiftMax = sharedPreferences.getInt("curlsAShiftMax", -1);
        curlsAShiftPlus = sharedPreferences.getInt("curlsAShiftPlus", -1);
        curlsLineWidthL = sharedPreferences.getInt("curlsLineWidthL", -1);
        curlsLineToNewLine = sharedPreferences.getInt("curlsLineToNewLine", -1);
        curlsTreesQntyCntr = sharedPreferences.getInt("curlsTreesQntyCntr", -1);
    }
    private void resumePreferences () {
        if (curlsImWidth != -1) {
            textViewCurlsImWidth.setText(String.valueOf(curlsImWidth));
            seekBarCurlsImSizeX.setProgress(curlsImWidth);
        }
        if (curlsImheight != -1) {
            textViewCurlsImHeight.setText(String.valueOf(curlsImheight));
            seekBarCurlsImSizeY.setProgress(curlsImheight);
        }
        if (curlsSteps != -1) {
            textViewCurlsSteps.setText(String.valueOf(curlsSteps));
            seekBarCurlsStepsQnty.setProgress(curlsSteps);
        }
        if (curlsBranchLenght != -1) {
            textViewCurlsBranchLenght.setText(String.valueOf(curlsBranchLenght));
            seekBarCurlsBranchLen.setProgress(curlsBranchLenght);
        }
        if (curlsAShiftMax != -1) {
            textViewCurlsAShiftMax.setText(String.valueOf(curlsAShiftMax));
            seekBarCurlsAngleShiftLim.setProgress(curlsAShiftMax);
        }
        if (curlsAShiftPlus != -1) {
            textViewAShiftPlus.setText(String.valueOf(curlsAShiftPlus));
            seekBarCurlsAngleShiftPlus.setProgress(curlsAShiftPlus);
        }
        if (curlsLineWidthL != -1) {
            textViewCurlsLineWidthL.setText(String.valueOf(curlsLineWidthL));
            seekBarCurlsLineWidth.setProgress(curlsLineWidthL);
        }
        if (curlsLineToNewLine != -1) {
            textViewLineToNewLine.setText(String.valueOf(curlsLineToNewLine));
            seekBarLineToNewLine.setProgress(curlsLineToNewLine);
        }
        if (curlsTreesQntyCntr != -1) {
            textViewCurlsTreesQntyCntr.setText(String.valueOf(curlsTreesQntyCntr));
            seekBarCurlsTreesQnty.setProgress(curlsTreesQntyCntr);
        }
    }
    private void resumeColors () {
        if (startColorR != -1) {
            seekBarCurlsStartColorR.setProgress(startColorR);
            updateColorIndicatorStart();
        }
        if (startColorG != -1) {
            seekBarCurlsStartColorG.setProgress(startColorG);
            updateColorIndicatorStart();
        }
        if (startColorB != -1) {
            seekBarCurlsStartColorB.setProgress(startColorB);
            updateColorIndicatorStart();
        }
        if (finishColorR != -1) {
            seekBarCurlsFinishColorR.setProgress(finishColorR);
            updateColorIndicatorFinish();
        }
        if (finishColorG != -1) {
            seekBarCurlsFinishColorG.setProgress(finishColorG);
            updateColorIndicatorFinish();
        }
        if (finishColorB != -1) {
            seekBarCurlsFinishColorB.setProgress(finishColorB);
            updateColorIndicatorFinish();
        }
        if (backColorR != -1) {
            seekBarCurlsBackColR.setProgress(backColorR);
            updateBackColorIndicatorBack();
        }
        if (backColorG != -1) {
            seekBarCurlsBackColG.setProgress(backColorG);
            updateBackColorIndicatorBack();
        }
        if (backColorB != -1) {
            seekBarCurlsBackColB.setProgress(backColorB);
            updateBackColorIndicatorBack();
        }
    }
    private void setListenerForOneBarSeekBar (SeekBar.OnSeekBarChangeListener listener) {
        seekBarCurlsImSizeX.setOnSeekBarChangeListener(listener);
        seekBarCurlsImSizeY.setOnSeekBarChangeListener(listener);
        seekBarCurlsStepsQnty.setOnSeekBarChangeListener(listener);
        seekBarCurlsBranchLen.setOnSeekBarChangeListener(listener);
        seekBarCurlsAngleShiftLim.setOnSeekBarChangeListener(listener);
        seekBarCurlsAngleShiftPlus.setOnSeekBarChangeListener(listener);
        seekBarCurlsLineWidth.setOnSeekBarChangeListener(listener);
        seekBarLineToNewLine.setOnSeekBarChangeListener(listener);
        seekBarCurlsTreesQnty.setOnSeekBarChangeListener(listener);
    }
    private void putOnPauseValues (SharedPreferences.Editor editor) {
        editor.putInt("curlsImWidth", seekBarCurlsImSizeX.getProgress());
        editor.putInt("curlsImheight", seekBarCurlsImSizeY.getProgress());
        editor.putInt("curlsSteps", seekBarCurlsStepsQnty.getProgress());
        editor.putInt("curlsBranchLenght", seekBarCurlsBranchLen.getProgress());
        editor.putInt("curlsAShiftMax", seekBarCurlsAngleShiftLim.getProgress());
        editor.putInt("curlsAShiftPlus", seekBarCurlsAngleShiftPlus.getProgress());
        editor.putInt("curlsLineWidthL", seekBarCurlsLineWidth.getProgress());
        editor.putInt("curlsLineToNewLine", seekBarLineToNewLine.getProgress());
        editor.putInt("curlsTreesQntyCntr", seekBarCurlsTreesQnty.getProgress());

        editor.putInt("startColorR", startColorR);
        editor.putInt("startColorG", startColorG);
        editor.putInt("startColorB", startColorB);
        editor.putInt("finishColorR", finishColorR);
        editor.putInt("finishColorG", finishColorG);
        editor.putInt("finishColorB", finishColorB);
        editor.putInt("backColorR", backColorR);
        editor.putInt("backColorG", backColorG);
        editor.putInt("backColorB", backColorB);
        editor.apply();
    }
}