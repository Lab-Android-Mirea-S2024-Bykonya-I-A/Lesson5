package com.mirea.bykonyaia.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.mirea.bykonyaia.mireaproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DistanceSensorFragment.IDistanceSensorValueChangedListener {
    static final private int REQUEST_CODE_PERMISSION = 200;
    private DistanceSensorFragment distance_sensor = null;
    private ProfilePhotoFragment profile_photo = null;
    private VoiceRecordFragment profile_voice = null;
    private boolean is_permissions_granted = false;
    private ActivityMainBinding binding = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        distance_sensor = (DistanceSensorFragment) getSupportFragmentManager().findFragmentById(R.id.distance_sensor);
        profile_photo = (ProfilePhotoFragment) getSupportFragmentManager().findFragmentById(R.id.profile_photo);
        profile_voice = (VoiceRecordFragment) getSupportFragmentManager().findFragmentById(R.id.profile_voice);
        distance_sensor.onValueChanged = this;
        MakePermissionsRequest();
    }


    private void MakePermissionsRequest() {
        final boolean storage_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final boolean camera_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if(storage_enabled && camera_enabled) {
            is_permissions_granted = true;
        } else {
            is_permissions_granted = false;
            ActivityCompat.requestPermissions(this,
                    new	String[] { Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	REQUEST_CODE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("", "onRequestPermissionsResult: " + String.valueOf(requestCode));
        if(requestCode == REQUEST_CODE_PERMISSION) {
            is_permissions_granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        } else {
            finish();
        }
    }

    @Override
    public void ValueChanged(float value) {
        Log.i("he-he", String.format("Distance changed: %.2f", value));
        if(value < 4) {
            profile_voice.StartRecord();
        }
    }
}