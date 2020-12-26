package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.Picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreatePictureActivity extends AppCompatActivity {

    private static final String TAG = "INSPI_DEBUG_TAG_CPA";

    private ImageView picture;

    private EditText pictureTitle;

    private Bitmap bitmap;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_picture);

        picture = findViewById(R.id.imageViewCreatePicture);
        pictureTitle = findViewById(R.id.editTextCreatePicture);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            bitmap = (Bitmap) extra.get("data");
        } else {
            Log.e(TAG, "Extra was empty");
        }
        picture.setImageBitmap(bitmap);
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
     * Generates an id for each picture.
     * @return returns an integer.
     */
    public int idFactory() {
        return counter++;
    }

    /**
     * Saves the picture publicly.
     * @param view is needed to use it by onClick() of activity_create_picture.
     */
    public void save(View view) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        Picture picture = new Picture(getAddress(), pictureTitle.getText().toString(), idFactory(), bitmap);
        File myPath = new File(directory, picture.getPictureName() + ".jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreatePictureActivity.this, PictureGalleryActivity.class);
        intent.putExtra("PICTURE_NAME", picture.getPictureName());
        intent.putExtra("PICTURE_ID", picture.getPictureID());
        intent.putExtra("PICTURE_TITLE", picture.getPictureTitle());
        startActivity(intent);
    }
}