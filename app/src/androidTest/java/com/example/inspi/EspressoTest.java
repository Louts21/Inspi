package com.example.inspi;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.inspi.controller.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * This is the test class of espresso tests.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Checks if the connectionButton works.
     */
    @Test
    public void mainNetworkButton() {
        Espresso.onView(withId(R.id.connectionButton)).perform(click());
    }

    /**
     * Checks if the memoButton works.
     */
    @Test
    public void mainCreateMemo() {
        Espresso.onView(withId(R.id.memoBotton)).perform(click());
    }

    /**
     * Checks if the memoGalleryButton works.
     */
    @Test
    public void mainMemoGallery() {
        Espresso.onView(withId(R.id.memoGalleryButton)).perform(click());
    }

    /**
     * Checks if the cameraButton works.
     */
    @Test
    public void mainCamera() {
        Espresso.onView(withId(R.id.cameraButton)).perform(click());
    }

    /**
     * Checks if the pictureGalleryButton works.
     */
    @Test
    public void mainPictureGallery() {
        Espresso.onView(withId(R.id.pictureGalleryButton)).perform(click());
    }
}
