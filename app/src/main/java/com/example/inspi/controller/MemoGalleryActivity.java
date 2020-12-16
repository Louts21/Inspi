package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private EditText memoEditText1;

    /**
     * EditText-object which allows me to change the visibility
     * of an object of activity_memo_gallery.
     */
    private EditText memoEditText2;

    /**
     * Button-object which allows me to change the visibility
     * of an object of activity_memo_gallery.
     */
    private Button memoEditButton1;

    /**
     * Button-object which allows me to change the visibility
     * of an object of activity_memo_gallery.
     */
    private Button memoSaveButton2;

    /**
     * Button-object which allows me to change the visibility
     * of an object of activity_memo_gallery.
     */
    private Button memoDeleteButton3;

    /**
     * Saves the title of a memo.
     */
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_gallery);
        memoTextView = findViewById(R.id.memoSearchResult);
        memoEditText1 = findViewById(R.id.memoSearch);
        memoEditText2 = findViewById(R.id.editTextMemoGallery);
        memoEditButton1 = findViewById(R.id.editButton);
        memoSaveButton2 = findViewById(R.id.saveGalleryButton);
        memoDeleteButton3 = findViewById(R.id.cancelButton);
        memoDeleteButton3.setVisibility(View.INVISIBLE);
        memoEditButton1.setVisibility(View.INVISIBLE);
        memoEditText2.setVisibility(View.INVISIBLE);
        memoSaveButton2.setVisibility(View.INVISIBLE);
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
            if (s.contains(memoEditText1.getText())) {
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
        } else {
            memoDeleteButton3.setVisibility(View.VISIBLE);
            memoEditButton1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Deletes a specific file.
     * @param view needed to use the onClick() method of activity_memo_gallery.
     */
    public void openDelete(View view) {
        String[] file = context.fileList();
        int counter1 = 0;
        int counter2 = 0;
        for (String fileName: file) {
            if (fileName.contains(memoEditText1.getText())) {
                counter1--;
                context.deleteFile(fileName);
            } else {
                counter1++;
                counter2++;
            }
        }
        if (counter1 == counter2) {
            Toast.makeText(MemoGalleryActivity.this, "Could not be found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MemoGalleryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            memoEditButton1.setVisibility(View.INVISIBLE);
            memoDeleteButton3.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method which allows the user to recreate a memo.
     * @param view is needed to use onClick() of activity_memo_gallery.
     * @throws FileNotFoundException might be thrown by finding the file.
     */
    public void openEdit(View view) throws FileNotFoundException {
        String[] file = context.fileList();
        int counter1 = 0;
        int counter2 = 0;
        for (String fileName: file) {
            if (fileName.contains(memoEditText1.getText())) {
                counter1--;
                FileInputStream fileInputStream = context.openFileInput(fileName);
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
                    memoEditText2.setText(stringBuilder.toString());
                    this.fileName = fileName;
                }
            } else {
                counter1++;
                counter2++;
            }
        }
        if (counter1 == counter2) {
            Toast.makeText(MemoGalleryActivity.this, "Could not be found", Toast.LENGTH_SHORT).show();
        } else {
            memoTextView.setVisibility(View.INVISIBLE);
            memoEditText2.setVisibility(View.VISIBLE);
            memoEditButton1.setVisibility(View.INVISIBLE);
            memoSaveButton2.setVisibility(View.VISIBLE);
            Toast.makeText(MemoGalleryActivity.this, "Could be found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Will be used after the user clicked Edit on activity_memo_gallery.
     * @param view is needed to use onClick() on activity_memo_gallery.
     */
    @SuppressLint("SetTextI18n")
    public void openSave(View view) {
        context.deleteFile(fileName);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        @SuppressLint("HardwareIds") File file = new File(info.getMacAddress(), fileName);
        try (FileOutputStream fos = context.openFileOutput(file.getFileName(), Context.MODE_PRIVATE)) {
            fos.write(memoEditText2.getText().toString().getBytes());
            Toast.makeText(MemoGalleryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            memoTextView.setVisibility(View.VISIBLE);
            memoEditText2.setVisibility(View.INVISIBLE);
            memoEditButton1.setVisibility(View.INVISIBLE);
            memoSaveButton2.setVisibility(View.INVISIBLE);
            memoDeleteButton3.setVisibility(View.INVISIBLE);
        }
    }
}