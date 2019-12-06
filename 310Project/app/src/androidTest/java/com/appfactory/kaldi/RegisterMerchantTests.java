package com.appfactory.kaldi;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
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
public class RegisterMerchantTests {

    @Rule
    public ActivityTestRule<RegisterMerchantActivity> activityRule = new ActivityTestRule<>(RegisterMerchantActivity.class);

    @Test
    public void registerMerchantCorrect(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000000);
        String emailString = randomNumber + "@gmail.com";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MerchantMainActivity.class.getName(), null, false);

        onView(withId(R.id.adminInput)).perform(typeText("Name Test"), closeSoftKeyboard());
        onView(withId(R.id.storeInput)).perform(typeText("store test"));
        onView(withId(R.id.emailInput)).perform(typeText(emailString), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.addressInput)).perform(typeText("address test"), closeSoftKeyboard());
        onView(withId(R.id.initialItemInput)).perform(typeText("item test"), closeSoftKeyboard());
        onView(withId(R.id.caffeineInput)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.addBusiness)).perform(click());

        Activity merchantMain = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(merchantMain);

        merchantMain.finish();
    }

    @Test
    public void registerMerchantIncorrectPassword(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000000);
        String emailString = randomNumber + "@gmail.com";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MerchantMainActivity.class.getName(), null, false);

        onView(withId(R.id.adminInput)).perform(typeText("Name Test"), closeSoftKeyboard());
        onView(withId(R.id.storeInput)).perform(typeText("store test"));
        onView(withId(R.id.emailInput)).perform(typeText(emailString), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("wrong password"), closeSoftKeyboard());
        onView(withId(R.id.addressInput)).perform(typeText("address test"), closeSoftKeyboard());
        onView(withId(R.id.initialItemInput)).perform(typeText("item test"), closeSoftKeyboard());
        onView(withId(R.id.caffeineInput)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.addBusiness)).perform(click());

        Activity merchantMain = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNull(merchantMain);
    }

    @Test
    public void registerMerchantIncorrectEmail(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000000);
        //String emailString = randomNumber + "@gmail.com";

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MerchantMainActivity.class.getName(), null, false);

        onView(withId(R.id.adminInput)).perform(typeText("Name Test"), closeSoftKeyboard());
        onView(withId(R.id.storeInput)).perform(typeText("store test"));
        onView(withId(R.id.emailInput)).perform(typeText("wrong email format"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("wrong password"), closeSoftKeyboard());
        onView(withId(R.id.addressInput)).perform(typeText("address test"), closeSoftKeyboard());
        onView(withId(R.id.initialItemInput)).perform(typeText("item test"), closeSoftKeyboard());
        onView(withId(R.id.caffeineInput)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.addBusiness)).perform(click());

        Activity merchantMain = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNull(merchantMain);
    }


}