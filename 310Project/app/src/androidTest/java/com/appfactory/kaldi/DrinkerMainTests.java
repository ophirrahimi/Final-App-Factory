package com.appfactory.kaldi;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class DrinkerMainTests {

    @Rule
    public ActivityTestRule<DrinkerMainActivity> activityRule = new ActivityTestRule<>(DrinkerMainActivity.class);

    @Test
    public void manageProfileTest(){
        //onView(withId(R.id.manageStore)).check(matches(isClickable()));
        Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(ManageProfileActivity.class.getName(), null, false);

        onView(withId(R.id.manageProfile)).perform(click());

        Activity manageProfile = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);

        assertNotNull(manageProfile);

        manageProfile.finish();
    }

    //Order History button redirects to order history page
    @Test
    public void orderHistoryTest(){
        Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(OrderHistoryActivity.class.getName(), null, false);

        onView(withId(R.id.history)).perform(click());

        Activity orderHistory = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);

        assertNotNull(orderHistory);

        orderHistory.finish();
    }
}
