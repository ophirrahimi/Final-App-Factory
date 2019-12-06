package com.appfactory.kaldi;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class RegisterButtonTests {

    @Rule
    public ActivityTestRule<Register> activityRule = new ActivityTestRule<>(Register.class);

    //Student button redirects to the correct page
    @Test
    public void drinkerButtonTest(){
        Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(RegisterDrinkerActivity.class.getName(), null, false);

        onView(withId(R.id.drinkerButton)).perform(click());

        Activity drinkerRegistration = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);

        assertNotNull(drinkerRegistration);

        drinkerRegistration.finish();

    }

    //Merchant button redirects to the correct page
    @Test
    public void merchantButtonTest() {
        Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(RegisterMerchantActivity.class.getName(), null, false);

        onView(withId(R.id.merchantButton)).perform(click());

        Activity merchantRegistration = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);

        assertNotNull(merchantRegistration);

        merchantRegistration.finish();
    }
}
