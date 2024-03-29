package com.mirea.bykonyaia.mireaproject;
import android.app.Activity;
import android.content.Intent;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfilePhotoFragment extends Fragment implements ActivityResultCallback<ActivityResult> {
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = null;
    private FragmentProfilePhotoBinding binding = null;
    private Uri store_profile_photo_uri;
    public ProfilePhotoFragment() { }

    @NonNull
    public static ProfilePhotoFragment newInstance() {
        ProfilePhotoFragment fragment = new ProfilePhotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("he-he", "onCreateView");
        binding = FragmentProfilePhotoBinding.inflate(inflater, container, false);
        cameraActivityResultLauncher = getActivity().registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this);
        binding.updateProfilePhotoButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnUpdatePhotoButtonClicked(view);
                }
            }
        );

        return binding.getRoot();
    }
    private Uri getStorageFileUri() throws IOException {
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile("temp_user_profile_photo", ".jpg", storageDirectory);
        String authorities = "com.mirea.asd.Lab5.camera.fileprovider";
        return FileProvider.getUriForFile(getActivity(), authorities, file);
    }


    public void OnUpdatePhotoButtonClicked(View v) {
        try {
            store_profile_photo_uri = getStorageFileUri();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, store_profile_photo_uri);
            cameraActivityResultLauncher.launch(cameraIntent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onActivityResult(ActivityResult o) {
        if(o.getResultCode() == Activity.RESULT_OK)
            binding.profilePhotoView.setImageURI(store_profile_photo_uri);
    }
}