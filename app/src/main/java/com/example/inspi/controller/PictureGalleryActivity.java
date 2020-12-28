package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inspi.R;

import java.util.HashMap;
import java.util.Map;

public class PictureGalleryActivity extends AppCompatActivity {

    private Map<String, String> pictureMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);
    }
}