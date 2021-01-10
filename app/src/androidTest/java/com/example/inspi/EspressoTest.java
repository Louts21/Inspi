package com.example.inspi;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.inspi.controller.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static org.hamcrest.Matchers.not;

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
    public void mainNetwork() {
        Espresso.onView(withId(R.id.connectionButton)).perform(click());

        Espresso.onView(withId(R.id.scanButton)).check(matches(withText("Scan for devices")));
        Espresso.onView(withId(R.id.discoverableButton)).check(matches(withText("Activate discoverability")));
        Espresso.onView(withId(R.id.connectButton)).check(matches(withText("Connect")));
    }

    /**
     * Checks if the memoButton works.
     */
    @Test
    public void mainCreateMemo() throws InterruptedException {
        Espresso.onView(withId(R.id.memoBotton)).perform(click());

        Espresso.onView(withId(R.id.memoTitle)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.memoTextField)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.memoTextField)).perform(clearText());
        Espresso.onView(withId(R.id.memoTitle)).perform(clearText());
        Espresso.onView(withId(R.id.saveButton)).check(matches(withText("Save")));
        Espresso.onView(withId(R.id.memoTitle)).perform(typeText("Empire Awaris"));

        Espresso.onView(withId(R.id.memoTitle)).check(matches(withText("Empire Awaris")));
        Espresso.onView(withId(R.id.memoTextField)).perform(replaceText("Empire Awaris was founded..."));

        Espresso.onView(withId(R.id.memoTextField)).check(matches(withText("Empire Awaris was founded...")));
        Espresso.onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Checks if the memoGalleryButton works.
     */
    @Test
    public void mainMemoGallery() {
        Espresso.onView(withId(R.id.memoGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.memoSearch)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.button_open)).check(matches(withText("Open")));
        Espresso.onView(withId(R.id.memoSearchResult)).check(matches(withText("Please write a name and press Open.")));

        Espresso.onView(withId(R.id.memoSearch)).perform(clearText());
        Espresso.onView(withId(R.id.memoSearch)).perform(typeText("Empire Awaris"));
        Espresso.onView(withId(R.id.button_open)).perform(click());

        Espresso.onView(withId(R.id.editButton)).check(matches(withText("Edit")));
        Espresso.onView(withId(R.id.cancelButton)).check(matches(withText("Delete")));
        Espresso.onView(withId(R.id.editButton)).perform(click());

        Espresso.onView(withId(R.id.saveGalleryButton)).check(matches(withText("Save")));
        Espresso.onView(withId(R.id.editTextMemoGallery)).perform(replaceText("Empire Awaris was founded?"));
        Espresso.onView(withId(R.id.saveGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.button_open)).perform(click());
        Espresso.onView(withId(R.id.cancelButton)).perform(click());
    }

    /**
     * Checks if the cameraButton works.
     * Problem i cant access the camera buttons.
     */
    @Test
    public void mainCamera() {
        Espresso.onView(withId(R.id.cameraButton)).perform(click());
    }

    /**
     * Checks if the pictureGalleryButton works.
     * It wont find a picture. It will fail if there appears one.
     */
    @Test
    public void mainPictureGallery() {
        Espresso.onView(withId(R.id.pictureGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.editTextPictureGallery)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.openButtonPictureGallery)).check(matches(withText("Open")));
        Espresso.onView(withId(R.id.deleteButtonPictureGallery)).check(matches(withText("Delete")));
        Espresso.onView(withId(R.id.editTextPictureGallery)).perform(clearText());
        Espresso.onView(withId(R.id.editTextPictureGallery)).perform(typeText("Louts"));
        Espresso.onView(withId(R.id.openButtonPictureGallery)).perform(click());

        Espresso.onView(withId(R.id.imageViewPictureGallery)).check(matches(not(isDisplayed())));
    }

    /**
     * This method will test everything in my application which is testable.
     */
    @Test
    public void E2ETest() {
        // Creating a memo
        Espresso.onView(withId(R.id.memoBotton)).perform(click());

        Espresso.onView(withId(R.id.memoTitle)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.memoTextField)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.memoTextField)).perform(clearText());
        Espresso.onView(withId(R.id.memoTitle)).perform(clearText());
        Espresso.onView(withId(R.id.saveButton)).check(matches(withText("Save")));
        Espresso.onView(withId(R.id.memoTitle)).perform(typeText("Empire Awaris"));

        Espresso.onView(withId(R.id.memoTitle)).check(matches(withText("Empire Awaris")));
        Espresso.onView(withId(R.id.memoTextField)).perform(replaceText("Empire Awaris was founded..."));

        Espresso.onView(withId(R.id.memoTextField)).check(matches(withText("Empire Awaris was founded...")));
        Espresso.onView(withId(R.id.saveButton)).perform(click());

        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        // Find that memo
        Espresso.onView(withId(R.id.memoGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.memoSearch)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.button_open)).check(matches(withText("Open")));
        Espresso.onView(withId(R.id.memoSearchResult)).check(matches(withText("Please write a name and press Open.")));
        Espresso.onView(withId(R.id.cancelButton)).check(matches(not(isEnabled())));
        Espresso.onView(withId(R.id.saveGalleryButton)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.editButton)).check(matches(not(isEnabled())));
        Espresso.onView(withId(R.id.editTextMemoGallery)).check(matches(not(isDisplayed())));

        Espresso.onView(withId(R.id.memoSearch)).perform(clearText());
        Espresso.onView(withId(R.id.memoSearch)).perform(typeText("Empire Awaris"));
        Espresso.onView(withId(R.id.button_open)).perform(click());

        Espresso.onView(withId(R.id.cancelButton)).check(matches(isEnabled()));
        Espresso.onView(withId(R.id.editButton)).check(matches(isEnabled()));
        Espresso.onView(withId(R.id.editButton)).check(matches(withText("Edit")));
        Espresso.onView(withId(R.id.cancelButton)).check(matches(withText("Delete")));
        Espresso.onView(withId(R.id.editButton)).perform(click());

        Espresso.onView(withId(R.id.saveGalleryButton)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.editTextMemoGallery)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.saveGalleryButton)).check(matches(withText("Save")));
        Espresso.onView(withId(R.id.editTextMemoGallery)).perform(replaceText("Empire Awaris was founded?"));
        Espresso.onView(withId(R.id.saveGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.button_open)).perform(click());
        Espresso.onView(withId(R.id.cancelButton)).perform(click());

        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        // Use Bluetooth
        Espresso.onView(withId(R.id.connectionButton)).perform(click());

        Espresso.onView(withId(R.id.scanButton)).check(matches(withText("Scan for devices")));
        Espresso.onView(withId(R.id.discoverableButton)).check(matches(withText("Activate discoverability")));
        Espresso.onView(withId(R.id.connectButton)).check(matches(withText("Connect")));

        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        // Find a picture
        Espresso.onView(withId(R.id.pictureGalleryButton)).perform(click());

        Espresso.onView(withId(R.id.editTextPictureGallery)).check(matches(withText("Empty")));
        Espresso.onView(withId(R.id.openButtonPictureGallery)).check(matches(withText("Open")));
        Espresso.onView(withId(R.id.deleteButtonPictureGallery)).check(matches(withText("Delete")));
        Espresso.onView(withId(R.id.editTextPictureGallery)).perform(typeText("Louts"));

        Espresso.onView(withId(R.id.imageViewPictureGallery)).check(matches(not(isDisplayed())));

        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
        Espresso.onView(isRoot()).perform(ViewActions.pressBack());
    }
}
