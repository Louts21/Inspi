package com.example.inspi;

import com.example.inspi.model.File;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest {
    @Test
    public void nameCreateTest() {
        File file = new File("Lorbeeren Weg 77", "Mein Weg nach Rom.");
        Assert.assertTrue(file.getFileName().contains("2020"));
    }
}
