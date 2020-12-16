package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.inspi.R;

public class PictureGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        recyclerView = findViewById(R.id.recyclerViewPictureGallery);
        recyclerView.setHasFixedSize(true);
    }
}