package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.inspi.R;
import com.example.inspi.adapter.PGAAdapter;

import java.util.HashMap;
import java.util.Map;

public class PictureGalleryActivity extends AppCompatActivity {

    //private RecyclerView recyclerView;

    private Map<String, String> pictureMap = new HashMap<>();

    //private PGAAdapter pgaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        Intent intent = getIntent();
        if (intent.hasExtra("PICTURE_TITLE")) {
            pictureMap.put(intent.getStringExtra("PICTURE_NAME"), intent.getStringExtra("PICTURE_TITLE"));
        }

        /*
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        pgaAdapter = new PGAAdapter(pictureMap);
        recyclerView.setAdapter(pgaAdapter);
         */
    }
}