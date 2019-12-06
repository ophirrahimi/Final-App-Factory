package com.appfactory.kaldi;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RegisterDrinkerTests {

    @Rule
    public ActivityTestRule<RegisterDrinkerActivity> activityRule = new ActivityTestRule<>(RegisterDrinkerActivity.class);

    @Test
    public void registerCorrectTest(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000000);
        String emailString = randomNumber + "@gmail.com";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(DrinkerMainActivity.class.getName(), null, false);

        onView(withId(R.id.adminInput)).perform(typeText("Name Test"), closeSoftKeyboard());
        onView(withId(R.id.emailInput)).perform(typeText(emailString), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.addBusiness)).perform(click());

        Activity drinkerMain = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(drinkerMain);

        drinkerMain.finish();
    }

    @Test
    public void registerIncorrectTest(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000000);
        String emailString = randomNumber + "@gmail.com";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(RegisterDrinkerActivity.class.getName(), null, false);

        onView(withId(R.id.adminInput)).perform(typeText("Name Test"), closeSoftKeyboard());
        onView(withId(R.id.emailInput)).perform(typeText(emailString), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("wrong password"), closeSoftKeyboard());
        onView(withId(R.id.addBusiness)).perform(click());

        Activity drinkerRegistration = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNull(drinkerRegistration);

    }


}
