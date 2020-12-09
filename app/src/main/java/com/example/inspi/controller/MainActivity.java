package com.example.inspi.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.inspi.R;

/**
 * This is the main class/activity.
 * Here starts the user of my application.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Request to take one picture.
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method opens the activity: CreateMemoActivity.
     * Which allows the user to create a memo/file.
     * @param view needs a view-object to use onClick().
     */
    public void openMemoCreate(View view) {
        Intent intent = new Intent(this, CreateMemoActivity.class);
        startActivity(intent);
    }

    /**
     * This method opens the activity: NetworkActivity.
     * Which allows the user to connect to another device and share his files and pictures.
     * @param view needs a view-object to use onClick().
     */
    public void openConnection(View view) {
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }

    /**
     * This method opens the activity: MemoGallery.
     * Which allows the user to view his memos/files.
     * @param view needs a view-object to use onClick().
     */
    public void openMemoGallery(View view) {
        Intent intent = new Intent(this, MemoGalleryActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ShowToast")
    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                }
            }
        }
    }
}