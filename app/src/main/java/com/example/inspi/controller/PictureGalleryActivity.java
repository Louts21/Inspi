package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
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

public class PictureGalleryActivity extends AppCompatActivity {

    private static final String TAG = "INSPI_DEBUG_TAG_PGA";

    private Button saveButton;

    private Button editButton;

    private Button deleteButton;

    private Button openButton;

    private TextView seePictureTitle;

    private TextView createdPictures;

    private ImageView imageView;

    private EditText renameEditText;

    private Bitmap bitmap;

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        saveButton = findViewById(R.id.saveButtonPictureGallery);
        saveButton.setEnabled(false);
        saveButton.setVisibility(View.INVISIBLE);
        editButton = findViewById(R.id.editButtonPictureGallery);
        editButton.setEnabled(false);
        editButton.setVisibility(View.INVISIBLE);
        deleteButton = findViewById(R.id.deleteButtonPictureGallery);
        deleteButton.setEnabled(false);
        deleteButton.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageViewPictureGallery);
        imageView.setVisibility(View.INVISIBLE);
        seePictureTitle = findViewById(R.id.textViewPictureTitlePictureGallery);
        seePictureTitle.setVisibility(View.INVISIBLE);
        renameEditText = findViewById(R.id.editTextPictureGallery);
        renameEditText.setVisibility(View.INVISIBLE);
        renameEditText.setEnabled(false);
        openButton = findViewById(R.id.openButtonPictureGallery);

        createdPictures = findViewById(R.id.textViewPictureGallery);
        String[] files = this.fileList();
        for (String pictureTitle : files) {
            if (pictureTitle.contains("Pic")) {
                createdPictures.setText(showInput(pictureTitle));
            }
        }
    }

    /**
     * Allows the user to find his pictures.
     * @param view is needed to use onClick() in the XML-File.
     */
    public void openPicture(View view) {
        createdPictures.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        seePictureTitle.setVisibility(View.VISIBLE);
        EditText userInput = findViewById(R.id.editTextPictureGallery);

        int counter1 = 0;
        int counter2 = 0;
        String[] files = this.fileList();
        for (String pictureTitle : files) {
            if (pictureTitle.contains(userInput.getText().toString())) {
                counter1--;
                seePictureTitle.setText(showInput(pictureTitle));
            } else {
                counter1++;
                counter2++;
            }
        }

        if (counter1 == counter2) {
            Toast.makeText(this, "Picture could not be found", Toast.LENGTH_SHORT).show();
            createdPictures.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            seePictureTitle.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(this, "Picture found", Toast.LENGTH_SHORT).show();
            bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/Files" + File.separator + userInput.getText().toString() + ".jpg");
            imageView.setImageBitmap(bitmap);
            saveButton.setVisibility(View.INVISIBLE);
            saveButton.setEnabled(false);
            editButton.setVisibility(View.VISIBLE);
            editButton.setEnabled(true);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setEnabled(true);
            openButton.setVisibility(View.INVISIBLE);
            openButton.setEnabled(false);
        }
    }

    /**
     * Changes the visibility and dis- or enables some objects.
     * @param view is needed to use onClick() in the XML-File.
     */
    public void editPictureTitle(View view) {
        editButton.setVisibility(View.INVISIBLE);
        editButton.setEnabled(false);
        renameEditText.setVisibility(View.VISIBLE);
        renameEditText.setEnabled(true);
        seePictureTitle.setVisibility(View.INVISIBLE);
        seePictureTitle.setEnabled(false);
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setEnabled(true);
    }

    public void deletePictureAndTitle(View view) {

    }

    /**
     * Saves the new title of the given picture.
     * @param view is needed to use onClick() in the XML-File.
     */
    public void saveChanges(View view) {
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setEnabled(false);
        seePictureTitle.setVisibility(View.VISIBLE);
        seePictureTitle.setEnabled(true);

        this.deleteFile(fileName);

        Picture picture = new Picture(getAddress(), renameEditText.getText().toString(), bitmap);
        try (FileOutputStream fos = this.openFileOutput(picture.getPictureName(), Context.MODE_PRIVATE)) {
            fos.write(renameEditText.getText().toString().getBytes());
            Toast.makeText(PictureGalleryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            openButton.setVisibility(View.VISIBLE);
            openButton.setEnabled(true);
            createdPictures.setVisibility(View.VISIBLE);
            createdPictures.setEnabled(true);
            imageView.setVisibility(View.INVISIBLE);
            seePictureTitle.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Reducing Code duplication.
     * It simply gets the data of a file which has been saved in context.
     * @param pictureTitle Is needed to know which file we are looking for.
     * @return returns a String of the information a specific file.
     */
    private String showInput(String pictureTitle) {
        FileInputStream fis = null;
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
        fileName = stringBuilder.toString();
        return stringBuilder.toString();
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
}