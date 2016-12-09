package com.inserteffect.spickerfx;


import com.inserteffect.spickerfx.settings.Settings;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import static com.inserteffect.spickerfx.util.Views.changeAlpha;
import static com.inserteffect.spickerfx.util.Views.disableView;
import static com.inserteffect.spickerfx.util.Views.setChecked;
import static com.inserteffect.spickerfx.util.Views.fullscreen;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximity;
    private Sensor accelerometer;

    private boolean isLocked;
    private float maxAlpha = 1f;

    private EditText textInput;
    private SwitchCompat lock;
    private View toolbar;
    private SeekBar brightness;

    private Settings settings;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            settings.saveText(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        private float currentAlpha;

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textInput.setAlpha(seekBar.getProgress() / 100f);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            currentAlpha = textInput.getAlpha();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            maxAlpha = seekBar.getProgress() / 100f;
            textInput.setAlpha(currentAlpha);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initSettings();
        initListeners();
        initSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY: {
                if (sensorEvent.values[0] == 0) {
                    onShow();
                } else {
                    onHide();
                }
                break;
            }
            case Sensor.TYPE_ACCELEROMETER: {
                if (sensorEvent.values[2] > 30) {
                    onReset();
                }
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isLocked) {
            fullscreen(getWindow(), true);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void findViews() {
        textInput = (EditText) findViewById(R.id.text_input);
        lock = (SwitchCompat) findViewById(R.id.lock);
        toolbar = findViewById(R.id.tool_bar);
        brightness = (SeekBar) findViewById(R.id.brightness);
    }

    private void initSettings() {
        settings = new Settings(this);

        textInput.setText(settings.loadText());
        textInput.setSelection(textInput.getText().length());
    }

    private void initListeners() {
        lock.setOnCheckedChangeListener((compoundButton, checked) -> setLocked(checked));
        textInput.addTextChangedListener(textWatcher);
        brightness.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void setLocked(boolean locked) {
        isLocked = locked;

        final float alpha = (locked) ? 0 : 1;

        changeAlpha(textInput, alpha);
        changeAlpha(toolbar, alpha);

        disableView(textInput, locked);
        disableView(toolbar, locked);

        fullscreen(getWindow(), locked);
    }

    private void onShow() {
        if (isLocked) {
            changeAlpha(textInput, maxAlpha);
        }
    }

    private void onHide() {
        if (isLocked) {
            changeAlpha(textInput, 0);
        }
    }

    private void onReset() {
        setChecked(lock, false);
    }
}
