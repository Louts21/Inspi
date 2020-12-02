package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inspi.R;

import java.io.File;

public class CreateMemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
    }

    private int memoNumber;

    /**
     * Will be called when the user hits the save-button
     * Should save the created memo
     */
    public void saveMemo(View view) {
        EditText editText = findViewById(R.id.memoTextField);
        if (!editText.getText().toString().isEmpty()) {
            File file = new File(CreateMemoActivity.this.getFilesDir(), "memos");
            Toast.makeText(CreateMemoActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}