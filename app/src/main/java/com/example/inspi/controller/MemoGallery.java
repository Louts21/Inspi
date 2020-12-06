package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MemoGallery extends AppCompatActivity {

    private Context context;

    private TextView memoTextView;

    private EditText memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_gallery);
        memoTextView = findViewById(R.id.memoSearchResult);
        memoEditText = findViewById(R.id.memoSearch);
        context = getApplicationContext();
    }

    public void findMemoAndOpen(View view) throws FileNotFoundException {
        String[] files = context.fileList();
        for (String s: files) {
            if (s.contains(memoEditText.getText())) {
                Toast.makeText(MemoGallery.this, "Could be found", Toast.LENGTH_SHORT).show();
                FileInputStream fileInputStream = context.openFileInput(s);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    memoTextView.setText(stringBuilder.toString());
                }
            } else {
                Toast.makeText(MemoGallery.this, "Could not be found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}