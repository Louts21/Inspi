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
     *
     */
    private final Integer pictureID;

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
    public Picture(String address, String memo, int id) {
        pictureID = id;
        text = memo;
        pictureName = currentTimeGetter() + " " + address;
    }

    @Override
    public String currentTimeGetter() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(pictureCalendar);
    }

    /**
     * Setter of pictureText (String-variable).
     * @param newText is the new memo for the picture.
     */
    public void setText(String newText) {
        text = newText;
    }

    /**
     * Getter of pictureName (String-variable).
     * @return returns a String.
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * Getter of text (String-variable).
     * @return returns a String.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter of pictureID (Integer variable).
     * @return returns an integer.
     */
    public Integer getPictureID() {
        return pictureID;
    }
}
