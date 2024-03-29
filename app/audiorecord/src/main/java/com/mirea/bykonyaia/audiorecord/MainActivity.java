package com.mirea.bykonyaia.audiorecord;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.mirea.bykonyaia.audiorecord.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 200;
    private ActivityMainBinding binding = null;
    private boolean isWork;

    private MainActivityState state = MainActivityState.IDLE;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String recordFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final boolean audioRecordPermissionStatus = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        final boolean storagePermissionStatus = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus && storagePermissionStatus) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        recordFilePath = (new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp")).getAbsolutePath();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION)
            isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (!isWork) finish();
    }

    public void OnRecordButtonClicked(View v) {
        switch (state) {
            case IDLE: {
                StartRecord();
                break;
            }
            case RECORD: {
                StopRecord();
                break;
            }
            case PLAYING: { break; }
            default: { break; }
        }
    }
    public void OnPlayingButtonClicked(View v) {
        switch (state) {
            case IDLE: {
                StartPlaying();
                break;
            }
            case PLAYING: {
                StopPlaying();
                break;
            }
            case RECORD: { break; }
            default: { break; }
        }
    }


    private void StartPlaying() {
        state = MainActivityState.PLAYING;
        binding.recordButton.setEnabled(false);
        binding.playingButton.setEnabled(true);

        try	{
            player = new MediaPlayer();
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("he-he", "prepare() failed");
        }
    }
    private void StopPlaying() {
        state = MainActivityState.IDLE;
        binding.recordButton.setEnabled(true);
        binding.playingButton.setEnabled(true);

        player.release();
        player = null;
    }
    private void StartRecord() {
        state = MainActivityState.RECORD;
        binding.recordButton.setEnabled(true);
        binding.playingButton.setEnabled(false);

        try	{
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(recordFilePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
        } catch (IOException e)	{
            Log.e("he-he",	"prepare()	failed");
        }
    }
    private void StopRecord() {
        state = MainActivityState.IDLE;
        binding.recordButton.setEnabled(true);
        binding.playingButton.setEnabled(true);

        recorder.stop();
        recorder.release();
        recorder = null;
    }
}