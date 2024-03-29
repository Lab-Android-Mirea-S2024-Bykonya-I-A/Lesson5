package com.mirea.bykonyaia.accelerometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.mirea.bykonyaia.accelerometer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensors_manager = null;
    private ActivityMainBinding binding = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensors_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensors_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensors_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensors_manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            binding.xValueLabel.setText(String.valueOf(sensorEvent.values[0]));
            binding.yValueLabel.setText(String.valueOf(sensorEvent.values[1]));
            binding.zValueLabel.setText(String.valueOf(sensorEvent.values[2]));
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        binding.accuracyValueLabel.setText(String.valueOf(i));
    }
}