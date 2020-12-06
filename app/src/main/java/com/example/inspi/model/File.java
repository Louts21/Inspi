package com.example.inspi.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class File implements IFModel {

    @Override
    public String currentTimeGetter() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(calendar.getTime());
    }

    public String getFileName(String address) {
        return currentTimeGetter() + " " + address;
    }
}
