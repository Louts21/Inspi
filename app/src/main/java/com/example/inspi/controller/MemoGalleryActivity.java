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

/**
 * This class shows all memos the user has created.
 */
public class MemoGalleryActivity extends AppCompatActivity {

    /**
     * This will be the context-object of the current device.
     */
    private Context context;

    /**
     * We need this textView-object to manipulate
     * the one on the view object of activity_memo_gallery.
     */
    private TextView memoTextView;

    /**
     * We need this editText-object to manipulate
     * the one on the view object of activity_memo_gallery.
     */
    private EditText memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_gallery);
        memoTextView = findViewById(R.id.memoSearchResult);
        memoEditText = findViewById(R.id.memoSearch);
        context = getApplicationContext();
    }

    /**
     * This class can find your memo and open it.
     * It will tell you if it could'nt find it.
     * @param view is needed to activate it onClick().
     * @throws FileNotFoundException might be thrown if it cant find the file.
     */
    public void findMemoAndOpen(View view) throws FileNotFoundException {
        String[] files = context.fileList();
        int counter1 = 0;
        int counter2 = 0;
        for (String s: files) {
            if (s.contains(memoEditText.getText())) {
                counter1--;
                Toast.makeText(MemoGalleryActivity.this, "Could be found", Toast.LENGTH_SHORT).show();
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
                counter1++;
                counter2++;
            }
        }
        if (counter1 == counter2) {
            Toast.makeText(MemoGalleryActivity.this, "Could not be found", Toast.LENGTH_SHORT).show();
        }
    }
}