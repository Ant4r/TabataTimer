package com.example.david.tabatatimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.example.david.tabatatimer.data.Tabataconfig;
import com.example.david.tabatatimer.data.TabataconfigDAO;


import java.util.List;

public class Tabata extends AppCompatActivity {


    static final int PICK_CONFIG_REQUEST = 1;
    //VIEW
    private TextView currentTimerLabel;
    private TextView currentTimer;
    private TextView prepareValue;
    private TextView workValue;
    private TextView restValue;
    private TextView cyclesValue;
    private TextView tabatasValue;
    private Button saveButton;
    private Button loadButton;
    private Button stopButton;
    private Button startButton;
    private Button pauseButton;
    //DATA
    private Tabataconfig saveOfCurrentConfig;
    private Tabataconfig currentConfig = new Tabataconfig("Default", 5, 5, 3, 4, 1);
    private long updatedTime;
    private CountDownTimer timer;
    private String currentTimerName;
    private String saveCurrentTimerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata);

        // Récupérer les view
        currentTimerLabel = (TextView) findViewById(R.id.CurrentTimerLabel);
        currentTimer = (TextView) findViewById(R.id.Timer);
        prepareValue = (TextView) findViewById(R.id.PrepareValue);
        workValue = (TextView) findViewById(R.id.WorkValue);
        restValue = (TextView) findViewById(R.id.RestValue);
        cyclesValue = (TextView) findViewById(R.id.CyclesValue);
        tabatasValue = (TextView) findViewById(R.id.TabatasValue);
        saveButton = (Button) findViewById(R.id.SaveButton);
        loadButton = (Button) findViewById(R.id.LoadButton);
        stopButton = (Button) findViewById(R.id.StopButton);
        startButton = (Button) findViewById(R.id.StartButton);
        pauseButton = (Button) findViewById(R.id.PauseButton);

        currentTimerName = "Ready to launch!";
        miseAJour();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (currentTimerName.equals("Ready to launch!") || currentTimerName.equals("Paused!")){
            savedInstanceState.putBoolean("isRunning",false);
        }
        else {
            savedInstanceState.putBoolean("isRunning",true);
            pause(pauseButton);
        }
        savedInstanceState.putParcelable("saveOfCurrentConfig",saveOfCurrentConfig);
        savedInstanceState.putParcelable("currentConfig", currentConfig);
        savedInstanceState.putLong("updatedTime", updatedTime);
        savedInstanceState.putString("currentTimerName", currentTimerName);
        savedInstanceState.putString("saveCurrentTimerName", saveCurrentTimerName);


        super.onSaveInstanceState(savedInstanceState);
    }
    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        saveOfCurrentConfig = savedInstanceState.getParcelable("saveOfCurrentConfig");
        currentConfig = savedInstanceState.getParcelable("currentConfig");
        updatedTime = savedInstanceState.getLong("updatedTime");
        currentTimerName = savedInstanceState.getString("currentTimerName");
        saveCurrentTimerName = savedInstanceState.getString("saveCurrentTimerName");
        boolean isRunning = savedInstanceState.getBoolean("isRunning");
        if (isRunning){
            start(startButton);
        }
        miseAJour();
    }

    //Mise à jour Graphique
    private void miseAJour() {

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (updatedTime % 1000);

        // Affichage du "timer"
        currentTimer.setText("" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds));
        currentTimerLabel.setText(currentTimerName);

        prepareValue.setText(currentConfig.getPrepare() / 60 + ":" + String.format("%02d", currentConfig.getPrepare() % 60));
        workValue.setText("" + currentConfig.getWork() / 60 + ":" + String.format("%02d", currentConfig.getWork() % 60));
        restValue.setText(currentConfig.getRest() / 60 + ":" + String.format("%02d", currentConfig.getRest() % 60));
        cyclesValue.setText(Integer.toString(currentConfig.getCycles()));
        tabatasValue.setText(Integer.toString(currentConfig.getTabatas()));
    }

    public void save(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set the name of the config you want to save.");
        final EditText input = new EditText(this);
        input.setText(currentConfig.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Tabataconfig> configurations = TabataconfigDAO.selectAll();
                for (Tabataconfig config: configurations) {
                    if (input.getText().toString().equals(config.getName())){
                        currentConfig.setName(input.getText().toString());
                        config.copy(currentConfig);
                        config.save();
                        break;
                    }
                }
                if (!(input.getText().toString().equals(currentConfig.getName()))){
                    currentConfig.setName(input.getText().toString());
                    currentConfig = new Tabataconfig(currentConfig);
                    currentConfig.save();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void start(View view) {
        if (currentTimerName.equals("Ready to launch!")) {
            saveOfCurrentConfig = new Tabataconfig(currentConfig);
            currentTimerName = "Prepare";
            launch_Timer();

        } else if (currentTimerName.equals("Paused!")) {
            launch_Timer();
        }
    }

    public void launch_Timer() {

        if (currentTimerName.equals("Prepare")) updatedTime = currentConfig.getPrepare() * 1000;
        else if (currentTimerName.equals("Work")) updatedTime = currentConfig.getWork() * 1000;
        else if (currentTimerName.equals("Rest")) updatedTime = currentConfig.getRest() * 1000;
        else if (currentTimerName.equals("Paused!")) currentTimerName = saveCurrentTimerName;

        timer = new CountDownTimer(updatedTime, 10) {

            public void onTick(long millisUntilFinished) {
                updatedTime = millisUntilFinished;
                miseAJour();
            }

            public void onFinish() {
                updatedTime = 0;
                if (currentTimerName.equals("Prepare")) {
                    currentTimerName = "Work";
                    launch_Timer();
                } else if (currentTimerName.equals("Work")) {
                    currentTimerName = "Rest";
                    launch_Timer();
                } else if (currentTimerName.equals("Rest") && (currentConfig.getCycles() >= 0 || currentConfig.getTabatas() >= 0)) {
                    if (currentConfig.getCycles() == 0 && currentConfig.getTabatas() > 1) {
                        currentConfig.setTabatas(currentConfig.getTabatas() - 1);
                        currentConfig.setCycles(saveOfCurrentConfig.getCycles());
                        currentTimerName = "Work";
                        launch_Timer();
                    } else if (currentConfig.getCycles() > 0) {
                        currentConfig.setCycles(currentConfig.getCycles() - 1);
                        currentTimerName = "Work";
                        launch_Timer();
                    } else if (currentConfig.getCycles() == 0 && currentConfig.getTabatas() == 1) {
                        currentConfig = saveOfCurrentConfig;
                        currentTimerName = "Ready to launch!";
                    }
                    miseAJour();
                }
            }

        }.start();
    }

    public void pause(View view) {
        if (timer != null && !currentTimerName.equals("Paused!")) {
            timer.cancel();
            saveCurrentTimerName = new String(currentTimerName);
            currentTimerName = "Paused!";
            miseAJour();
        }
    }

    public void stop(View view) {
        if (timer != null) {
            timer.cancel();
            currentConfig = saveOfCurrentConfig;
            currentTimerName = "Ready to launch!";
            updatedTime = 0;
            miseAJour();
        }
    }

    public void load(View view) {
        Intent intent = new Intent(this, TabataConfigList.class);
        startActivityForResult(intent, PICK_CONFIG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONFIG_REQUEST) {
            if (resultCode==RESULT_OK){
                currentConfig =(Tabataconfig) data.getParcelableExtra("Tabataconfig");
                miseAJour();
            }
        }
    }

    public void setParam(View view) {

        int viewID = view.getId();

        if (viewID == R.id.PrepareLayout) {

            final NumberPicker picker = new NumberPicker(Tabata.this);
            picker.setMinValue(1);
            picker.setMaxValue(1000);
            picker.setValue(currentConfig.getPrepare());

            final FrameLayout layout = new FrameLayout(Tabata.this);
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(Tabata.this)
                    .setView(layout)
                    .setTitle("Set the prepare time")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentConfig.setPrepare(picker.getValue());
                            miseAJour();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        } else if (viewID == R.id.WorkLayout) {

            final NumberPicker picker = new NumberPicker(Tabata.this);
            picker.setMinValue(1);
            picker.setMaxValue(1000);
            picker.setValue(currentConfig.getWork());

            final FrameLayout layout = new FrameLayout(Tabata.this);
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(Tabata.this)
                    .setView(layout)
                    .setTitle("Set the work time")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentConfig.setWork(picker.getValue());
                            miseAJour();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        } else if (viewID == R.id.RestLayout) {

            final NumberPicker picker = new NumberPicker(Tabata.this);
            picker.setMinValue(1);
            picker.setMaxValue(1000);
            picker.setValue(currentConfig.getRest());

            final FrameLayout layout = new FrameLayout(Tabata.this);
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(Tabata.this)
                    .setView(layout)
                    .setTitle("Set the rest time")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentConfig.setRest(picker.getValue());
                            miseAJour();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        } else if (viewID == R.id.CyclesLayout) {

            final NumberPicker picker = new NumberPicker(Tabata.this);
            picker.setMinValue(1);
            picker.setMaxValue(1000);
            picker.setValue(currentConfig.getCycles());

            final FrameLayout layout = new FrameLayout(Tabata.this);
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(Tabata.this)
                    .setView(layout)
                    .setTitle("Set the number of cycles")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentConfig.setCycles(picker.getValue());
                            miseAJour();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        } else if (viewID == R.id.TabatasLayout) {

            final NumberPicker picker = new NumberPicker(Tabata.this);
            picker.setMinValue(1);
            picker.setMaxValue(1000);
            picker.setValue(currentConfig.getTabatas());

            final FrameLayout layout = new FrameLayout(Tabata.this);
            layout.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));

            new AlertDialog.Builder(Tabata.this)
                    .setView(layout)
                    .setTitle("Set the number of tabatas")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentConfig.setTabatas(picker.getValue());
                            miseAJour();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }

    }


}
