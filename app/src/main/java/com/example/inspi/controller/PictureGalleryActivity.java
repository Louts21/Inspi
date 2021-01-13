package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.Picture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * This activity shows, deletes or saves a new title of a picture.
 * @author Kevin Jagielski
 */
public class PictureGalleryActivity extends AppCompatActivity {
    /**
     * A tag which will be shown if a Log appears with important information.
     */
    private static final String TAG = "INSPI_DEBUG_TAG_PGA";

    /**
     * Button of R.id.deleteButtonPictureGallery.
     */
    private Button deleteButton;

    /**
     * Button of R.id.openButtonPictureGallery.
     */
    private Button openButton;

    /**
     * Button of R.id.cancelButtonPictureGallery.
     */
    private Button cancelButton;

    /**
     * TextView of R.id.textViewPictureGallery.
     */
    private TextView createdPictures;

    /**
     * ImageView of R.id.imageViewPictureGallery.
     */
    private ImageView imageView;

    /**
     * Bitmap which will be shown in imageView (object).
     */
    private Bitmap bitmap;

    /**
     * A string of letters which is needed to remember which file we use right now.
     */
    private String fileTitle;

    /**
     * A string of letters which present the current path we use to get a picture.
     */
    private String fileDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        deleteButton = findViewById(R.id.deleteButtonPictureGallery);
        deleteButton.setEnabled(false);
        deleteButton.setVisibility(View.INVISIBLE);

        imageView = findViewById(R.id.imageViewPictureGallery);
        imageView.setVisibility(View.INVISIBLE);

        openButton = findViewById(R.id.openButtonPictureGallery);

        cancelButton = findViewById(R.id.cancelButtonPictureGallery);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setEnabled(false);

        setCreatedPictures();
    }

    /**
     * Allows the user to find his pictures.
     * @param view is needed to use onClick() in the XML-File.
     */
    public void openPicture(View view) {
        EditText userInput = findViewById(R.id.editTextPictureGallery);

        int counter1 = 0;
        int counter2 = 0;
        String[] files = this.fileList();
        for (String pictureTitle : files) {
            if (pictureTitle.contains(userInput.getText().toString())) {
                counter1--;
            } else {
                counter1++;
                counter2++;
            }
        }

        if (counter1 == counter2) {
            Toast.makeText(this, "Picture could not be found", Toast.LENGTH_SHORT).show();
            createdPictures.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            fileDirectory = Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/Files" + File.separator + userInput.getText().toString() + ".jpg";
            bitmap = BitmapFactory.decodeFile(fileDirectory);

            if (bitmap == null) {
                Toast.makeText(this, "Picture could not be found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Picture found", Toast.LENGTH_SHORT).show();
                imageView.setImageBitmap(bitmap);
                createdPictures.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setEnabled(true);

                openButton.setVisibility(View.INVISIBLE);
                openButton.setEnabled(false);
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setEnabled(true);
            }
        }
    }

    /**
     * Deletes the shown picture and his reference in context.
     * @param view is needed use it through onClick() in the XML-File.
     */
    public void deletePictureAndTitle(View view) {
        int counter1 = 0;
        int counter2 = 0;

        java.io.File fileDelete = new java.io.File(fileDirectory);
        if (fileDelete.exists()) {
            if (fileDelete.delete()) {
                counter1++;
                counter2++;
                Log.e(TAG, "File deleted :" + fileDirectory);
                callBroadCast();
            } else {
                counter1--;
                Log.e(TAG, "File not deleted :" + fileDirectory);
            }
        }

        if (this.deleteFile(fileTitle)) {
            counter1++;
            counter2++;
            Log.i(TAG, "File deleted at saveChanges()");
        } else {
            counter1--;
            Log.i(TAG, "Could not delete old file at saveChanges()");
        }

        if (counter1 == counter2) {
            Toast.makeText(this, "Picture deleted", Toast.LENGTH_SHORT).show();
            openButton.setVisibility(View.VISIBLE);
            openButton.setEnabled(true);
            createdPictures.setVisibility(View.VISIBLE);
            createdPictures.setEnabled(true);
            imageView.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            deleteButton.setEnabled(false);
            cancelButton.setVisibility(View.INVISIBLE);
            cancelButton.setEnabled(false);
            setCreatedPictures();
        } else {
            Toast.makeText(this, "Picture could not be deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Allows the user to change the picture.
     * @param view is needed to use onClick() in the XML-File.
     */
    public void cancelAction(View view) {
        openButton.setVisibility(View.VISIBLE);
        openButton.setEnabled(true);

        deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setEnabled(false);

        imageView.setVisibility(View.INVISIBLE);

        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setEnabled(false);

        createdPictures.setVisibility(View.VISIBLE);

        setCreatedPictures();
    }

    /**
     * Reducing Code duplication.
     * It simply gets the data of a file which has been saved in context.
     * @param pictureTitle Is needed to know which file we are looking for.
     * @return returns a String of the information a specific file.
     */
    private String showInput(String pictureTitle) {
        FileInputStream fis = null;
        fileTitle = pictureTitle;
        try {
            fis = this.openFileInput(pictureTitle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Shows all pictureTitles the user had created.
     */
    private void setCreatedPictures() {
        createdPictures = findViewById(R.id.textViewPictureGallery);
        String[] files = this.fileList();
        createdPictures.setText(null);
        for (String pictureTitle : files) {
            if (pictureTitle.contains("Pic")) {
                createdPictures.append(showInput(pictureTitle) + '\n');
            }
        }
    }

    /**
     * Informative method which shows what he exactly deleted and where.
     */
    private void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e(TAG, " >= 23");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 23");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
}