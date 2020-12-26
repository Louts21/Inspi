package com.example.inspi.model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

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
     * Title of a picture.
     */
    private String pictureTitle;

    /**
     * Id of each picture.
     */
    private final Integer pictureID;

    /**
     * Bitmap of each picture.
     */
    private final Bitmap pictureBitmap;

    /**
     * The current time and date.
     */
    private final Date pictureCalendar = Calendar.getInstance().getTime();

    /**
     * Constructor of Picture (class).
     * Allows creating objects of Picture (class).
     * @param address the MAC-Address of our device.
     * @param title a title related to the picture.
     * @param id a specific id for each picture.
     * @param bitmap is the bitmap of each picture.
     */
    public Picture(String address, String title, int id, Bitmap bitmap) {
        pictureID = id;
        pictureTitle = title;
        pictureBitmap = bitmap;
        pictureName = currentTimeGetter() + " " + address + " " + pictureTitle;
    }

    @Override
    public String currentTimeGetter() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(pictureCalendar);
    }

    /**
     * Getter of pictureName (String-variable).
     * @return returns a String.
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * Getter of pictureTitle (String-variable).
     * @return returns a String.
     */
    public String getPictureTitle() {
        return pictureTitle;
    }

    /**
     * Getter of pictureID (Integer variable).
     * @return returns an integer.
     */
    public Integer getPictureID() {
        return pictureID;
    }

    /**
     * Getter of pictureBitmap (Bitmap variable).
     * @return returns the bitmap of our picture.
     */
    public Bitmap getPictureBitmap() {
        return pictureBitmap;
    }
}
