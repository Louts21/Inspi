package com.example.inspi;

import com.example.inspi.model.File;
import com.example.inspi.model.Picture;

import org.junit.Assert;
import org.junit.Test;

/**
 * Class to test if specific function work properly.
 */
public class UnitTest {
    /**
     * Simple test to see if the creation of each file works good.
     */
    @Test
    public void memoCreateTest() {
        File file = new File("Lorbeeren Weg 77", "Mein Weg nach Rom.");
        Assert.assertTrue(file.getFileName().contains("2020"));
        Assert.assertTrue(file.getFileAddress().contains("Lorbeeren Weg 77"));
        Assert.assertTrue(file.getFileTitle().contains("Mein Weg nach Rom."));
    }

    /**
     * Simple test to see if the creation of each picture works good.
     */
    @Test
    public void pictureCreateTest() {
        Picture picture = new Picture("Lorbeeren Weg 77", "Urlaub am Meer", 22, null);
        Assert.assertTrue(picture.getPictureName().contains("2020"));
        Assert.assertEquals("Urlaub am Meer", picture.getPictureTitle());
        Assert.assertEquals(22, (int) picture.getPictureID());
    }
}
