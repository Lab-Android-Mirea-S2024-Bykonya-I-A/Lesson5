package com.mirea.bykonyaia.mireaproject;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.bykonyaia.mireaproject.databinding.ActivityMainBinding;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentProfilePhotoBinding;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentVoiceRecordBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoiceRecordFragment extends Fragment {
    private enum State{
        IDLE,
        RECORD,
        PLAYING
    }

    private FragmentVoiceRecordBinding binding = null;
    private String recordFilePath = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private State state = State.IDLE;

    @NonNull
    public static VoiceRecordFragment newInstance() {
        VoiceRecordFragment fragment = new VoiceRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVoiceRecordBinding.inflate(inflater, container, false);
        binding.recordVoiceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnRecordButtonClicked();
                    }
                }
        );
        binding.playVoiceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnPlayButtonClicked();
                    }
                }
        );

        recordFilePath = (new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp")).getAbsolutePath();
        return binding.getRoot();
    }

    private void OnRecordButtonClicked() {
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
    private void OnPlayButtonClicked() {
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


    public void StartPlaying() {
        if(state == State.PLAYING) return;
        state = State.PLAYING;
        binding.recordVoiceButton.setEnabled(false);
        binding.playVoiceButton.setEnabled(true);

        try	{
            player = new MediaPlayer();
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("he-he", "prepare() failed");
        }
    }
    public void StopPlaying() {
        if(state == State.IDLE) return;
        state = State.IDLE;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(true);

        player.release();
        player = null;
    }
    public void StartRecord() {
        if(state == State.RECORD) return;
        state = State.RECORD;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(false);

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
    public void StopRecord() {
        if(state == State.IDLE) return;
        state = State.IDLE;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(true);

        recorder.stop();
        recorder.release();
        recorder = null;
    }
}