package com.example.inspi.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Model class which saves the file name and the date of his creation.
 */
public class File implements IFModel {
    /**
     * The name of the file.
     */
    private final String fileName;

    /**
     * The title of the file which will be saved into the fileName (variable).
     */
    private final String fileTitle;

    /**
     * The address of the device which will be saved into the fileName (variable).
     */
    private final String fileAddress;

    /**
     * The date when the file name was created.
     */
    private final Date fileCalendar = Calendar.getInstance().getTime();

    /**
     * Constructor of File (class).
     * @param address MAC-Address of the current device.
     * @param title Title of the created memo.
     */
    public File(String address, String title) {
        fileTitle = title;
        fileAddress = address;
        fileName = currentTimeGetter() + " " + address + " " + title;
    }

    @Override
    public String currentTimeGetter() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(fileCalendar);
    }

    /**
     * Getter of fileName (variable).
     * @return returns a String.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Getter of fileAddress (variable).
     * @return returns a String.
     */
    public String getFileAddress() {
        return fileAddress;
    }

    /**
     * Getter of fileTitle (variable).
     * @return returns a String.
     */
    public String getFileTitle() {
        return fileTitle;
    }

    /**
     * Getter of fileCalendar (variable).
     * @return returns a Date-Object.
     */
    public Date getFileCalendar() {
        return fileCalendar;
    }
}
