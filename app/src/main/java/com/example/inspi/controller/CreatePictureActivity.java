package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.Picture;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePictureActivity extends AppCompatActivity {

    private static final String TAG = "INSPI_DEBUG_TAG_CPA";

    private ImageView picture;

    private EditText pictureTitle;

    private Bitmap bitmap;

    private byte[] byteArray;

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
     * Will delete the picture.
     * @param view needed to access it on the xml-file.
     */
    public void recycle(View view) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byteArray = baos.toByteArray();
        bitmap.recycle();
    }

    /**
     * Saves the picture publicly.
     * @param view is needed to use it by onClick() of activity_create_picture.
     */
    public void save(View view) {
        Picture picture = new Picture(getAddress(), pictureTitle.getText().toString(), bitmap);
        try (FileOutputStream fos = this.openFileOutput(picture.getPictureName(), MODE_PRIVATE)){
            fos.write(byteArray);
            fos.write(pictureTitle.getText().toString().getBytes());
            fos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreatePictureActivity.this, PictureGalleryActivity.class);
        startActivity(intent);
    }
}