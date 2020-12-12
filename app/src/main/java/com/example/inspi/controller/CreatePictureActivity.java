package com.example.inspi.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspi.R;
import com.example.inspi.model.Picture;

public class CreatePictureActivity extends AppCompatActivity {

    private ImageView picture;

    private EditText memoText;

    private EditText memoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_picture);

        picture = findViewById(R.id.imageViewCreatePicture);
        memoText = findViewById(R.id.editTextMCreatePicture);
        memoTitle = findViewById(R.id.editTextCreatePicture);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        Bitmap bitmap = (Bitmap) extra.get("data");
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
     * Saves the picture publicly.
     * @param view is needed to use it by onClick() of activity_create_picture.
     */
    @SuppressLint("ShowToast")
    public void save(View view) {
        Picture picture = new Picture(getAddress(), memoTitle.getText().toString());
        picture.setText(memoText.getText().toString());
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
    }
}