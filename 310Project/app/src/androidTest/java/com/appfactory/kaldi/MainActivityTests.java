package com.appfactory.kaldi;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    //User can enter text into email field on login page
    @Test
    public void loginTest() {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(DrinkerMainActivity.class.getName(), null, false);

        onView(withId(R.id.email)).perform(typeText("test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        Activity login = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNull(login);
    }

    //User can enter text into email field on login page
    @Test
    public void incorrectLoginTest() {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(DrinkerMainActivity.class.getName(), null, false);

        onView(withId(R.id.email)).perform(typeText("invalidemail@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        Activity login = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNull(login);
    }

    //Register button is clickable
    @Test
    public void registerButtonTest(){
        Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Register.class.getName(), null, false);

        onView(withId(R.id.regButton)).perform(click());

        Activity register = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);

        assertNotNull(register);

        register.finish();
    }

    @Test
    public void incorrectLoginTypeDrinker(){
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MerchantMainActivity.class.getName(), null, false);

        onView(withId(R.id.email)).perform(typeText("nicky@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());

        onView(withId(R.id.drinkerRadio)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Activity login = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNull(login);
    }

    @Test
    public void incorrectLoginTypeMerchant(){
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(DrinkerMainActivity.class.getName(), null, false);

        onView(withId(R.id.email)).perform(typeText("derek@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());

        onView(withId(R.id.merchantRadio)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Activity login = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNull(login);
    }
}
