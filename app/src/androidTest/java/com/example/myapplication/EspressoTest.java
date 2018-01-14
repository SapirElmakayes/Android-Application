package com.example.myapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    private String mNotExistUserName, mNotExistPassword ;

    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(Login.class);

    @Before
    public void initValidString()
    {
        // Specify a valid string.
        mNotExistUserName = "sapir1";
        mNotExistPassword = "123123";
    }

    @Test
    public void changeText_sameActivity()
    {
        // Type text and then press the button.
        onView(withId(R.id.etUserName)).perform(typeText(mNotExistUserName), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText(mNotExistPassword), closeSoftKeyboard());

        onView(withId(R.id.bLogin)).perform(click());

        // Check that the text was changed.
   //     onView(withText("").check(matches(withText("Username And/Or Password Is Incorrect")));

        onView(withId(R.id.textView2))
                .check(matches(withText("Hello sapir1")));

    }
}

