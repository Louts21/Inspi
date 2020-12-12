package com.example.inspi.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is for the model picture.
 */
public class Picture implements IFModel {
    /**
     * Name of the picture.
     */
    private final String pictureName;

    /**
     * Context to the picture.
     */
    private String text;

    /**
     * The current time and date.
     */
    private final Date pictureCalendar = Calendar.getInstance().getTime();

    /**
     * Constructor of Picture (class).
     * Allows creating objects of Picture (class).
     * @param address the MAC-Address of our device.
     * @param memo a memo related to the picture.
     */
    public Picture(String address, String memo) {
        text = memo;
        pictureName = currentTimeGetter() + " " + address;
    }

    @Override
    public String currentTimeGetter() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(pictureCalendar);
    }

    /**
     * Setter of pictureText (variable).
     * @param newText is the new memo for the picture.
     */
    public void setText(String newText) {
        text = newText;
    }

    /**
     * Getter of pictureName.
     * @return returns a String.
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * Getter of text (variable).
     * @return returns a String.
     */
    public String getText() {
        return text;
    }
}
