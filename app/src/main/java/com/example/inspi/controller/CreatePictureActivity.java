package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.File;
import com.example.inspi.model.Picture;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePictureActivity extends AppCompatActivity {

    private static final String TAG = "INSPI_DEBUG_TAG_CPA";

    private ImageView picture;

    private EditText pictureTitle;

    private Bitmap bitmap;

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
        bitmap.recycle();
        onBackPressed();
    }

    /**
     * Saves the picture privately.
     * data/data/YOUR_APP_PACKAGE/files/Inspi/Images/image.jpeg
     * @param view is needed to use it by onClick() of activity_create_picture.
     */
    public void save(View view) {
        java.io.File pictureFile = getOutputMediaFile();

        if (pictureFile == null) {
            Log.d(TAG,"Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(CreatePictureActivity.this, PictureGalleryActivity.class);
        startActivity(intent);
         */
    }

    /**
     * Creates a media file and returns it.
     * @return returns a file object.
     */
    private java.io.File getOutputMediaFile() {
        java.io.File mediaStorageDir = new java.io.File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/Files");

        if (! mediaStorageDir.exists()) {
            if (! mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        Picture picture = new Picture(getAddress(), pictureTitle.getText().toString(), bitmap);
        try (FileOutputStream fos = this.openFileOutput(picture.getPictureName(), Context.MODE_PRIVATE)) {
            fos.write(picture.getPictureTitle().getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        java.io.File mediaFile;
        String mImageName = picture.getPictureTitle() +".jpg";
        mediaFile = new java.io.File(mediaStorageDir.getPath() + java.io.File.separator + mImageName);
        return mediaFile;
    }
}