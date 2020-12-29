package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspi.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PictureGalleryActivity extends AppCompatActivity {

    private Button saveButton;

    private Button editButton;

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        saveButton = findViewById(R.id.saveButtonPictureGallery);
        saveButton.setEnabled(false);
        editButton = findViewById(R.id.editButtonPictureGallery);
        editButton.setEnabled(false);
        deleteButton = findViewById(R.id.deleteButtonPictureGallery);
        deleteButton.setEnabled(false);
    }

    public void openPicture(View view) {
        String[] stringArray = this.fileList();
        EditText userInput = findViewById(R.id.editTextPictureGallery);
        ImageView imageView = findViewById(R.id.imageViewPictureGallery);
        int counter1 = 0;
        int counter2 = 0;
        for (String file: stringArray) {
            if (file.contains("Pic" + userInput.getText().toString())) {
                counter1--;
                try {
                    FileInputStream fis = this.openFileInput(file);
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
            } else {
                counter1++;
                counter2++;
            }
        }
        if (counter1 == counter2) {
            Toast.makeText(this, "Picture not found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Picture found", Toast.LENGTH_SHORT).show();
        }
    }
}