package com.mirea.bykonyaia.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.bykonyaia.mireaproject.databinding.FragmentDistanceSensorBinding;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentVoiceRecordBinding;

public class DistanceSensorFragment extends Fragment implements SensorEventListener {
    public interface IDistanceSensorValueChangedListener {
        void ValueChanged(float value);
    }

    public IDistanceSensorValueChangedListener onValueChanged = null;
    private FragmentDistanceSensorBinding binding = null;
    private SensorManager sensors_manager = null;
    public DistanceSensorFragment() {}

    public static DistanceSensorFragment newInstance() {
        DistanceSensorFragment fragment = new DistanceSensorFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDistanceSensorBinding.inflate(inflater, container, false);
        sensors_manager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor sensor = sensors_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensors_manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensors_manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            binding.distanceValueView.setText(String.valueOf(sensorEvent.values[0]));
            Log.i("he-he", String.format("Distance changed: %.2f", sensorEvent.values[0]));
            if(onValueChanged != null) {
                Log.i("he-he", "\tSend");
                onValueChanged.ValueChanged(sensorEvent.values[0]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}