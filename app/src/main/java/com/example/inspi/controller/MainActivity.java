package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.example.inspi.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the main class/activity.
 * Here starts the user of my application.
 */
public class MainActivity extends AppCompatActivity {

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
        Intent intent = new Intent(this, MemoGallery.class);
        startActivity(intent);
    }
}