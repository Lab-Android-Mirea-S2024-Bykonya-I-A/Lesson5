package com.mirea.bykonyaia.task_1_1;
import com.mirea.bykonyaia.task_1_1.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityMainBinding binding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gravity_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravity_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        SetSensorInfo();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            Log.d("he-he", String.format("Gravity chagned: [%.2f, %.2f, %.2f]", sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private void SetSensorInfo() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors_info = sensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<HashMap<String, Object>> view_sensors = new ArrayList<>();
        for (Sensor sensor : sensors_info) {
            HashMap<String, Object> sensor_info = new HashMap<>();
            sensor_info.put("Name", sensor.getName());
            sensor_info.put("Value", sensor.getMaximumRange());
            view_sensors.add(sensor_info);
            Log.i("he-he", String.format("[%s][%f]", sensor.getName(), sensor.getMaximumRange()));
        }

        SimpleAdapter sensors_history = new SimpleAdapter(
                this, view_sensors, android.R.layout.simple_list_item_2,
                new String[]{ "Name", "Value" },
                new int[]{ android.R.id.text1, android.R.id.text2 }
        );
        binding.sensorsListView.setAdapter(sensors_history);
    }
}