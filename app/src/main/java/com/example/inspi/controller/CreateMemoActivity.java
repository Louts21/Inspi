package com.example.inspi.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.os.storage.StorageManager.ACTION_MANAGE_STORAGE;

/**
 * This class allows the user to create memos.
 */
public class CreateMemoActivity extends AppCompatActivity {

    /**
     * This long number declares an amount of mb for
     * the memos.
     */
    private static long NUM_BYTES_NEEDED_FOR_MY_APP;

    /**
     * This object is the context of the current device.
     */
    private Context context;

    /**
     * We need this editText-object to manipulate
     * the one on the view object of activity_create_memo.
     */
    private EditText memoEditText;

    /**
     * We need this editText-object to manipulate
     * the one on the view object of activity_create_memo.
     */
    private EditText memoTitleField;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
        memoEditText = findViewById(R.id.memoTextField);
        memoTitleField = findViewById(R.id.memoTitle);
        context = getApplicationContext();
        NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L;
        try {
            space();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method says the device how much space (MB) we need and claims it for us.
     * @throws IOException might throw an IOException.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void space() throws IOException {
        StorageManager storageManager = getApplicationContext().getSystemService(StorageManager.class);
        UUID appSpecificInternalDirUuid = storageManager.getUuidForPath(getFilesDir());
        long availableBytes = storageManager.getAllocatableBytes(appSpecificInternalDirUuid);
        if (availableBytes >= NUM_BYTES_NEEDED_FOR_MY_APP) {
            storageManager.allocateBytes(appSpecificInternalDirUuid, NUM_BYTES_NEEDED_FOR_MY_APP);
        } else {
            Intent storageIntent = new Intent();
            storageIntent.setAction(ACTION_MANAGE_STORAGE);
        }
    }

    /**
     * Let us get the MAC-Address of our device.
     * @return returns a String of the MAC-Address.
     */
    @SuppressLint("HardwareIds")
    public String getAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * Creates a file/memo and saves it into a folder.
     * It uses the model file.
     * @param view is a View-Object which we need to combine it to the button in the XML file.
     */
    public void openSave(View view) {
        File file = new File(getAddress(), memoTitleField.getText().toString());
        try (FileOutputStream fos = context.openFileOutput(file.getFileName(), Context.MODE_PRIVATE)) {
            fos.write(memoEditText.getText().toString().getBytes());
            Toast.makeText(CreateMemoActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}