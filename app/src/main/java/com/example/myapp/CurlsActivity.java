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

    @Override
    protected void onPause() {
        super.onPause();
        seekBarCurlsImSizeX = findViewById(R.id.seekBarCurlsImSizeX);
        seekBarCurlsImSizeY = findViewById(R.id.seekBarCurlsImSizeY);
        seekBarCurlsStepsQnty = findViewById(R.id.seekBarCurlsStepsQnty);
        seekBarCurlsBranchLen = findViewById(R.id.seekBarCurlsBranchLen);
        seekBarCurlsAngleShiftLim = findViewById(R.id.seekBarCurlsAngleShiftLim);
        seekBarCurlsAngleShiftPlus = findViewById(R.id.seekBarCurlsAngleShiftPlus);
        seekBarCurlsLineWidth = findViewById(R.id.seekBarCurlsLineWidth);
        seekBarLineToNewLine = findViewById(R.id.seekBarLineToNewLine);
        seekBarCurlsTreesQnty = findViewById(R.id.seekBarCurlsTreesQnty);

        int curlsImWidth = seekBarCurlsImSizeX.getProgress();
        int curlsImheight = seekBarCurlsImSizeY.getProgress();
        int curlsSteps = seekBarCurlsStepsQnty.getProgress();
        int curlsBranchLenght = seekBarCurlsBranchLen.getProgress();
        int curlsAShiftMax = seekBarCurlsAngleShiftLim.getProgress();
        int curlsAShiftPlus = seekBarCurlsAngleShiftPlus.getProgress();
        int curlsLineWidthL = seekBarCurlsLineWidth.getProgress();
        int curlsLineToNewLine = seekBarLineToNewLine.getProgress();
        int curlsTreesQntyCntr = seekBarCurlsTreesQnty.getProgress();

        SharedPreferences sharedPreferences = getSharedPreferences("CurlsSettings", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("curlsImWidth", curlsImWidth);
        editor.putInt("curlsImheight", curlsImheight);
        editor.putInt("curlsSteps", curlsSteps);
        editor.putInt("curlsBranchLenght", curlsBranchLenght);
        editor.putInt("curlsAShiftMax", curlsAShiftMax);
        editor.putInt("curlsAShiftPlus", curlsAShiftPlus);
        editor.putInt("curlsLineWidthL", curlsLineWidthL);
        editor.putInt("curlsLineToNewLine", curlsLineToNewLine);
        editor.putInt("curlsTreesQntyCntr", curlsTreesQntyCntr);

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

    @Override
    protected void onResume() {
        super.onResume();
        textViewCurlsImWidth = findViewById(R.id.textViewCurlsImWidth);
        textViewCurlsImHeight = findViewById(R.id.textViewCurlsImHeight);
        textViewCurlsSteps = findViewById(R.id.textViewCurlsSteps);
        textViewCurlsBranchLenght = findViewById(R.id.textViewCurlsBranchLenght);
        textViewCurlsAShiftMax = findViewById(R.id.textViewCurlsAShiftMax);
        textViewAShiftPlus = findViewById(R.id.textViewAShiftPlus);
        textViewCurlsLineWidthL = findViewById(R.id.textViewCurlsLineWidthL);
        textViewLineToNewLine = findViewById(R.id.textViewLineToNewLine);
        textViewCurlsTreesQntyCntr = findViewById(R.id.textViewCurlsTreesQntyCntr);

        seekBarCurlsImSizeX = findViewById(R.id.seekBarCurlsImSizeX);
        seekBarCurlsImSizeY = findViewById(R.id.seekBarCurlsImSizeY);
        seekBarCurlsStepsQnty = findViewById(R.id.seekBarCurlsStepsQnty);
        seekBarCurlsBranchLen = findViewById(R.id.seekBarCurlsBranchLen);
        seekBarCurlsAngleShiftLim = findViewById(R.id.seekBarCurlsAngleShiftLim);
        seekBarCurlsAngleShiftPlus = findViewById(R.id.seekBarCurlsAngleShiftPlus);
        seekBarCurlsLineWidth = findViewById(R.id.seekBarCurlsLineWidth);
        seekBarLineToNewLine = findViewById(R.id.seekBarLineToNewLine);
        seekBarCurlsTreesQnty = findViewById(R.id.seekBarCurlsTreesQnty);

        seekBarCurlsStartColorR = findViewById(R.id.seekBarCurlsStartColorR);
        seekBarCurlsStartColorG = findViewById(R.id.seekBarCurlsStartColorG);
        seekBarCurlsStartColorB = findViewById(R.id.seekBarCurlsStartColorB);
        seekBarCurlsFinishColorR = findViewById(R.id.seekBarCurlsFinishColorR);
        seekBarCurlsFinishColorG = findViewById(R.id.seekBarCurlsFinishColorG);
        seekBarCurlsFinishColorB = findViewById(R.id.seekBarCurlsFinishColorB);
        seekBarCurlsBackColR = findViewById(R.id.seekBarCurlsBackColR);
        seekBarCurlsBackColG = findViewById(R.id.seekBarCurlsBackColG);
        seekBarCurlsBackColB = findViewById(R.id.seekBarCurlsBackColB);

        SharedPreferences sharedPreferences = getSharedPreferences("CurlsSettings", MODE_PRIVATE);

        startColorR = sharedPreferences.getInt("startColorR", -1);
        startColorG = sharedPreferences.getInt("startColorG", -1);
        startColorB = sharedPreferences.getInt("startColorB", -1);
        finishColorR = sharedPreferences.getInt("finishColorR", -1);
        finishColorG = sharedPreferences.getInt("finishColorG", -1);
        finishColorB = sharedPreferences.getInt("finishColorB", -1);
        backColorR = sharedPreferences.getInt("backColorR", -1);
        backColorG = sharedPreferences.getInt("backColorG", -1);
        backColorB = sharedPreferences.getInt("backColorB", -1);

        int curlsImWidth = sharedPreferences.getInt("curlsImWidth", -1);
        int curlsImheight = sharedPreferences.getInt("curlsImheight", -1);
        int curlsSteps = sharedPreferences.getInt("curlsSteps", -1);
        int curlsBranchLenght = sharedPreferences.getInt("curlsBranchLenght", -1);
        int curlsAShiftMax = sharedPreferences.getInt("curlsAShiftMax", -1);
        int curlsAShiftPlus = sharedPreferences.getInt("curlsAShiftPlus", -1);
        int curlsLineWidthL = sharedPreferences.getInt("curlsLineWidthL", -1);
        int curlsLineToNewLine = sharedPreferences.getInt("curlsLineToNewLine", -1);
        int curlsTreesQntyCntr = sharedPreferences.getInt("curlsTreesQntyCntr", -1);

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curls);
        startColorR = 100;
        startColorG = 100;
        startColorB = 100;
        finishColorR = 100;
        finishColorG = 100;
        finishColorB = 100;
        backColorR = 100;
        backColorG = 100;
        backColorB = 100;



        seekBarCurlsImSizeX = findViewById(R.id.seekBarCurlsImSizeX);
        seekBarCurlsImSizeY = findViewById(R.id.seekBarCurlsImSizeY);
        seekBarCurlsStepsQnty = findViewById(R.id.seekBarCurlsStepsQnty);
        seekBarCurlsBranchLen = findViewById(R.id.seekBarCurlsBranchLen);
        seekBarCurlsAngleShiftLim = findViewById(R.id.seekBarCurlsAngleShiftLim);
        seekBarCurlsAngleShiftPlus = findViewById(R.id.seekBarCurlsAngleShiftPlus);
        seekBarCurlsLineWidth = findViewById(R.id.seekBarCurlsLineWidth);
        seekBarLineToNewLine = findViewById(R.id.seekBarLineToNewLine);
        seekBarCurlsTreesQnty = findViewById(R.id.seekBarCurlsTreesQnty);

        textViewCurlsImWidth = findViewById(R.id.textViewCurlsImWidth);
        textViewCurlsImHeight = findViewById(R.id.textViewCurlsImHeight);
        textViewCurlsSteps = findViewById(R.id.textViewCurlsSteps);
        textViewCurlsBranchLenght = findViewById(R.id.textViewCurlsBranchLenght);
        textViewCurlsAShiftMax = findViewById(R.id.textViewCurlsAShiftMax);
        textViewAShiftPlus = findViewById(R.id.textViewAShiftPlus);
        textViewCurlsLineWidthL = findViewById(R.id.textViewCurlsLineWidthL);
        textViewLineToNewLine = findViewById(R.id.textViewLineToNewLine);
        textViewCurlsTreesQntyCntr = findViewById(R.id.textViewCurlsTreesQntyCntr);





        seekBarCurlsStartColorR = findViewById(R.id.seekBarCurlsStartColorR);
        seekBarCurlsStartColorG = findViewById(R.id.seekBarCurlsStartColorG);
        seekBarCurlsStartColorB = findViewById(R.id.seekBarCurlsStartColorB);
        viewCurlsStartColor = findViewById(R.id.viewCurlsStartColor);

        seekBarCurlsFinishColorR = findViewById(R.id.seekBarCurlsFinishColorR);
        seekBarCurlsFinishColorG = findViewById(R.id.seekBarCurlsFinishColorG);
        seekBarCurlsFinishColorB = findViewById(R.id.seekBarCurlsFinishColorB);
        viewCurlsFinishColor = findViewById(R.id.viewCurlsFinishColor);

        seekBarCurlsBackColR = findViewById(R.id.seekBarCurlsBackColR);
        seekBarCurlsBackColG = findViewById(R.id.seekBarCurlsBackColG);
        seekBarCurlsBackColB = findViewById(R.id.seekBarCurlsBackColB);
        viewCurlsBackColor = findViewById(R.id.viewCurlsBackColor);

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
        seekBarCurlsImSizeX.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsImSizeY.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsStepsQnty.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsBranchLen.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsAngleShiftLim.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsAngleShiftPlus.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsLineWidth.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarLineToNewLine.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarCurlsTreesQnty.setOnSeekBarChangeListener(seekBarChangeListener);

        // private TextView textViewCurlsmageSizeX, textViewCurlsImageSizeY, textViewCurlsStepsQnty, textViewCurlsBranchLen;
        // private TextView textViewCurlsAngleShiftMax, textViewAngleShiftPlus, textViewCurlsLineWidth, textViewCurlsStartColor;
        // private TextView textViewCurlsFinishColor, textViewCurlsBackColor, textViewStarttoNewLine, textViewCurlsTreesQnty;
        // private TextView textViewCurlsHeader;

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

        textViewCurlsHeader = findViewById(R.id.textViewCurlsHeader);

        View.OnClickListener onClickListenerSizeX = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.image_width))
                        .setMessage(getResources().getString(R.string.explanation_size_x))
                        .show();
            }
        };
        View.OnClickListener onClickListenerSizeY = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.image_height))
                        .setMessage(getResources().getString(R.string.explanation_size_y))
                        .show();
            }
        };
        View.OnClickListener onClickListenerStepsQnty = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.steps_qnty))
                        .setMessage(getResources().getString(R.string.explanation_steps_qnty))
                        .show();
            }
        };
        View.OnClickListener onClickListenerBranchLenght = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.branch_lenght))
                        .setMessage(getResources().getString(R.string.explanation_branch_lenght))
                        .show();
            }
        };
        View.OnClickListener onClickListenerAngleShiftMax = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.angle_shift_max))
                        .setMessage(getResources().getString(R.string.explanation_angle_shift_max))
                        .show();
            }
        };
        View.OnClickListener onClickListenerAngleShiftPlus = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.angle_shift_plus))
                        .setMessage(getResources().getString(R.string.explanation_angle_shift_plus))
                        .show();
            }
        };
        View.OnClickListener onClickListenerLineWidth = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.line_width))
                        .setMessage(getResources().getString(R.string.explanation_line_width))
                        .show();
            }
        };
        View.OnClickListener onClickListenerStartColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.start_color))
                        .setMessage(getResources().getString(R.string.explanation_start_color))
                        .show();
            }
        };
        View.OnClickListener onClickListenerFinishColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.finish_color))
                        .setMessage(getResources().getString(R.string.explanation_finish_color))
                        .show();
            }
        };
        View.OnClickListener onClickListenerBackgroundColor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.background_color))
                        .setMessage(getResources().getString(R.string.explanation_background_color))
                        .show();
            }
        };
        View.OnClickListener onClickListenerLineToNewLine = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.lines_to_new_line))
                        .setMessage(getResources().getString(R.string.explanation_LTNL))
                        .show();
            }
        };
        View.OnClickListener onClickListenerTreesQnty = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.trees_qnty_exp))
                        .setMessage(getResources().getString(R.string.explanation_trees_qnty))
                        .show();
            }
        };
        View.OnClickListener onClickListenerCurlsDesc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurlsActivity.this)
                        .setTitle(getResources().getString(R.string.trees_qnty_exp))
                        .setMessage(getResources().getString(R.string.explanation_trees_qnty))
                        .show();
            }
        };

        textViewCurlsmageSizeX.setOnClickListener(onClickListenerSizeX);
        textViewCurlsImageSizeY.setOnClickListener(onClickListenerSizeY);
        textViewCurlsStepsQnty.setOnClickListener(onClickListenerStepsQnty);
        textViewCurlsBranchLen.setOnClickListener(onClickListenerBranchLenght);
        textViewCurlsAngleShiftMax.setOnClickListener(onClickListenerAngleShiftMax);
        textViewAngleShiftPlus.setOnClickListener(onClickListenerAngleShiftPlus);
        textViewCurlsLineWidth.setOnClickListener(onClickListenerLineWidth);
        textViewCurlsStartColor.setOnClickListener(onClickListenerStartColor);
        textViewCurlsFinishColor.setOnClickListener(onClickListenerFinishColor);
        textViewCurlsBackColor.setOnClickListener(onClickListenerBackgroundColor);
        textViewStarttoNewLine.setOnClickListener(onClickListenerLineToNewLine);
        textViewCurlsTreesQnty.setOnClickListener(onClickListenerTreesQnty);

        textViewCurlsHeader.setOnClickListener(onClickListenerCurlsDesc);




        Button backButton = (Button) findViewById(R.id.buttonCurlsBack);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurlsActivity.this, GeneratorsActicity.class);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener);


        SeekBar.OnSeekBarChangeListener onSeekBarChangeListenerStart = new SeekBar.OnSeekBarChangeListener() {
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
        seekBarCurlsStartColorR.setOnSeekBarChangeListener(onSeekBarChangeListenerStart);
        seekBarCurlsStartColorG.setOnSeekBarChangeListener(onSeekBarChangeListenerStart);
        seekBarCurlsStartColorB.setOnSeekBarChangeListener(onSeekBarChangeListenerStart);


        SeekBar.OnSeekBarChangeListener onSeekBarChangeListenerFinish = new SeekBar.OnSeekBarChangeListener() {
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
        seekBarCurlsFinishColorR.setOnSeekBarChangeListener(onSeekBarChangeListenerFinish);
        seekBarCurlsFinishColorG.setOnSeekBarChangeListener(onSeekBarChangeListenerFinish);
        seekBarCurlsFinishColorB.setOnSeekBarChangeListener(onSeekBarChangeListenerFinish);


        SeekBar.OnSeekBarChangeListener onSeekBarChangeListenerBack = new SeekBar.OnSeekBarChangeListener() {
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
        seekBarCurlsBackColR.setOnSeekBarChangeListener(onSeekBarChangeListenerBack);
        seekBarCurlsBackColG.setOnSeekBarChangeListener(onSeekBarChangeListenerBack);
        seekBarCurlsBackColB.setOnSeekBarChangeListener(onSeekBarChangeListenerBack);



        Button runButton = (Button) findViewById(R.id.buttonCurlsRun);
        View.OnClickListener onClickListenerRun = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runButton.setEnabled(false);
                backButton.setEnabled(false);
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                int stepQnty = seekBarCurlsStepsQnty.getProgress();
                int branchStartFromBranch = seekBarLineToNewLine.getProgress();
                int startColor = ((ColorDrawable) viewCurlsStartColor.getBackground()).getColor();
                int finishColor = ((ColorDrawable) viewCurlsFinishColor.getBackground()).getColor();
                int branchLenght = seekBarCurlsBranchLen.getProgress();
                int angleShiftLim = seekBarCurlsAngleShiftLim.getProgress();
                int angleShiftPlus = seekBarCurlsAngleShiftPlus.getProgress();
                int lineWidth = seekBarCurlsLineWidth.getProgress();
                boolean isTextureMode = true;
                int treesQnty = seekBarCurlsTreesQnty.getProgress();

                int backCol = ((ColorDrawable) viewCurlsBackColor.getBackground()).getColor();
                int width = seekBarCurlsImSizeX.getProgress();
                int height = seekBarCurlsImSizeY.getProgress();

                Callable<Bitmap> curlsGeneratorCallable = new CurlsGeneratorCallable(
                        stepQnty,
                        branchStartFromBranch,
                        startColor,
                        finishColor,
                        branchLenght,
                        angleShiftLim,
                        angleShiftPlus,
                        lineWidth,
                        true,
                        backCol,
                        width,
                        height,
                        treesQnty);
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
                                    startActivity(intent);
                                }
                            });
                        } catch (
                        ExecutionException e) {
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
}